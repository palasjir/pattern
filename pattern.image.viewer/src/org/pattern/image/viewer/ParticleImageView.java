package org.pattern.image.viewer;

import org.pattern.image.viewer.renderer.ParticleRenderer;
import org.pattern.image.viewer.renderer.RenderUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.pattern.data.Particle;
import org.pattern.data.ParticleImage;

/**
 * Contains panel with {@link ParticleImage} representation.
 *
 * @author palas
 */
public class ParticleImageView extends JScrollPane {

    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Cursor defCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor hndCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private final Zoomer zoomer;
    private final ImagePanel imagePanel;

    private ParticleImage pImage;
    private BufferedImage bufferedImage;
    private Callback callback;

    private boolean isSpaceBarDown = false;
    private boolean canScrollByDragging = false;
    private ParticleRenderer particleRenderer;
    private PopupProvider popupProvider;

    public ParticleImageView() {
        this.zoomer = new Zoomer();
        zoomer.registerListener(new Zoomer.Listener() {

            @Override
            public void zoomChanged() {
                if (bufferedImage != null) {
                    imagePanel.initTransform();
                    getViewport().setViewPosition(zoomer.getZoomedAt());
                    refresh();
                }
            }
        });
        this.imagePanel = new ImagePanel();
        setViewportView(imagePanel);
        zoomer.setZoomedAt(getViewport().getViewPosition());
        addComponentListener(componentAdapter);
    }

    public ParticleImageView(ParticleImage image) {
        this();
        pImage = image;
        bufferedImage = image.createBufferedImage();
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() > 0) {
                zoomer.out(e.getPoint());
            } else {
                zoomer.in(e.getPoint());
            }
        } else {
            super.processMouseWheelEvent(e);
        }
    }

    /**
     * * Repaints the image.
     */
    public void refresh() {
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    public Zoomer getZoomer() {
        return zoomer;
    }

    /**
     * When size changes it reposition image inside canvas.
     */
    private final ComponentAdapter componentAdapter = new ComponentAdapter() {

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            getViewport().setViewPosition(zoomer.getZoomedAt());
            refresh();
        }

    };

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setParticleRenderer(ParticleRenderer renderer) {
        this.particleRenderer = renderer;
    }

    public void setImage(ParticleImage image) {
        pImage = image;
        bufferedImage = image.createBufferedImage();
        imagePanel.setSize(pImage.getWidth(), pImage.getHeight());
        imagePanel.initTransform();
    }

    public interface Callback {
        public void onParticlesSelected(List<Particle> list, Point clicked, Point2D transformed);
    }
    
    public void setPopupProvider(PopupProvider provider){
        this.popupProvider = provider;
    }

    /**
     * Displays the image in a panel. Displays currently selected image in
     * Pattern Data. Main user interaction with image and particles is performed
     * here.
     *
     * @author palas
     */
    private class ImagePanel extends JPanel implements KeyListener {

        private RoiPicker mRoiPicker = null;

        protected AffineTransform atI2C;
        protected AffineTransform atC2I;

        public ImagePanel() {
            setBackground(BACKGROUND_COLOR);

            addMouseListener(mAdapter);
            addMouseMotionListener(mAdapter);
            addKeyListener(this);

            setFocusable(true);
            requestFocusInWindow();
        }

        private void initTransform() {
            int w = getWidth();
            int h = getHeight();

            int iw = bufferedImage.getWidth();
            int ih = bufferedImage.getHeight();

            double x = (w - zoomer.getValue() * iw) / 2;
            double y = (h - zoomer.getValue() * ih) / 2;
            atI2C = AffineTransform.getTranslateInstance(x, y);
            atI2C.scale(zoomer.getValue(), zoomer.getValue());

            try {
                atC2I = atI2C.createInverse();
            } catch (NoninvertibleTransformException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            if (bufferedImage != null) {
                g2.drawRenderedImage(bufferedImage, atI2C);

                // draw list of particles
                if (particleRenderer != null) {
                    AffineTransform save = g2.getTransform();
                    g2.transform(atI2C);
                    RenderUtil.draw(g2, particleRenderer, pImage.getParticles());
                    g2.setTransform(save);
                }

                // draw roi
                if (mRoiPicker != null) {
                    mRoiPicker.draw(g2);
                }
            } else {
                /* No image available */
                String str = "No image";
                Rectangle2D rect = g2.getFontMetrics().getStringBounds(str, g2);
                int xt = (int) (getWidth() / 2.0 - rect.getWidth() / 2.0);
                int yt = (int) (getHeight() / 2.0 - rect.getHeight() / 2.0);
                g2.drawString(str, xt, yt);
            }

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            if (zoomer != null && bufferedImage != null) {
                int w = (int) (zoomer.getValue() * bufferedImage.getWidth());
                int h = (int) (zoomer.getValue() * bufferedImage.getHeight());
                return new Dimension(w, h);
            }
            return super.getPreferredSize();
        }

        //<editor-fold defaultstate="collapsed" desc="Keyboard input">
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DELETE:
                    Action action = FileUtil.getConfigObject("Actions/Image/org-pattern-editor-action-DeleteSelectionAction.instance", Action.class);
                    if (action != null) {
                        action.actionPerformed(null);
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    e.getComponent().getParent().setCursor(hndCursor);
                    e.getComponent().getParent().repaint();
                    isSpaceBarDown = true;
                    break;

            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    isSpaceBarDown = false;
                    canScrollByDragging = false;
                    e.getComponent().getParent().setCursor(defCursor);
                    e.getComponent().getParent().repaint();
                    break;
            }
        }
            //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Picking">
        private void initRoi(Point point) {
            try {
                mRoiPicker = new RoiPicker(point, atI2C);
            } catch (NoninvertibleTransformException e) {
                System.out.println("Roi picker not instantiated!");
            }
        }

        private void updateRoi(Point point) {
            if (mRoiPicker != null) {
                mRoiPicker.update(point);
                repaint();
            }
        }

        private void endRoiPicking() {
            if (mRoiPicker != null) {
                List<Particle> found = mRoiPicker.find(pImage.getParticles());
                callback.onParticlesSelected(found, null, null);
            }
            mRoiPicker = null;
        }
        //</editor-fold>

        public Point2D toValues(Point point) {
            return atC2I.transform(
                    new Point2D.Double(point.x, point.y),
                    new Point2D.Double()
            );
        }

        private List<Particle> findParticles(Point point) {
            List<Particle> found = new ArrayList<>();
            Point2D transformed = toValues(point);
            for (Particle particle : pImage.getParticles()) {
                if (particle.contains(transformed)) {
                    found.add(particle);
                }
            }
            return found;
        }

        private void showPopupMenu(int x, int y) {
            if(popupProvider != null)
                popupProvider.getMenu().show(this, x, y);
        }

        //<editor-fold defaultstate="collapsed" desc="Mouse input">
        /**
         * Mouse adapter for ImagePanel. Handles mouse user interaction.
         */
        private final MouseInputAdapter mAdapter = new MouseInputAdapter() {

            private final Point pp = new Point();

            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true); // allow panel listen to key events
                requestFocusInWindow();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    List<Particle> found = findParticles(e.getPoint());
                    if(callback != null)
                        callback.onParticlesSelected(found, e.getPoint(), toValues(e.getPoint()));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setFocusable(true);
                requestFocusInWindow();
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopupMenu(e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (isSpaceBarDown) {
                        startScollByDragging(e);
                    } else {
                        initRoi(e.getPoint());
                        repaint();
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (canScrollByDragging) {
                    scrollByDragging(e);
                } else {
                    updateRoi(e.getPoint());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endRoiPicking();
                repaint();
            }

            private void startScollByDragging(MouseEvent e) {
                e = SwingUtilities.convertMouseEvent(
                        e.getComponent(), e, e.getComponent().getParent());
                pp.setLocation(e.getPoint());
                canScrollByDragging = true;
            }

            private void scrollByDragging(MouseEvent e) {
                JViewport viewport = ParticleImageView.this.getViewport();
                e = SwingUtilities.convertMouseEvent(
                        e.getComponent(), e, e.getComponent().getParent());
                Point cp = e.getPoint();
                Point vp = viewport.getViewPosition();
                vp.translate(pp.x - cp.x, pp.y - cp.y);
                //Rectangle rect = new Rectangle(vp, getViewport().getSize());
                if (vp.x > 0 && vp.y > 0) {
                    viewport.setViewPosition(vp);
                }
                pp.setLocation(cp);
            }

        };

    }
    //</editor-fold>

}
