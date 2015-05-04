/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ActionMap;
import javax.swing.JPopupMenu;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.pattern.classification.ui.action.CreateClassificatorAction;
import org.pattern.classification.ui.action.ImportClassificatorAction;

/**
 * Top component which displays list of classificators registered within pattern
 * application as an data instance.
 */
@TopComponent.Description(
        preferredID = "ClassificatorExplorerTC",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
        mode = "inspector", 
        openAtStartup = true, 
        position = 100
)
@ActionID(
        category = "Window", 
        id = "org.pattern.classification.ClassificatorExplorerTC"
)
@ActionReference(path = "Menu/Window")
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ClassificatorExplorerAction",
        preferredID = "ClassificatorExplorerTC"
)
@Messages({
    "CTL_ClassificatorExplorerAction=Classificators",
    "CTL_ClassificatorExplorerTC=Classificators",
    "HINT_ClassificatorExplorerTC=Displays projects clasificators"
})
public final class ClassificatorExplorerTC extends TopComponent implements ExplorerManager.Provider {

    private final ExplorerManager manager;

    public ClassificatorExplorerTC() {
        initComponents();
        setName(Bundle.CTL_ClassificatorExplorerTC());
        setToolTipText(Bundle.HINT_ClassificatorExplorerTC());
        manager = new ExplorerManager();
        ActionMap map = getActionMap();
        map.put("delete", ExplorerUtils.actionDelete(manager, true));
        associateLookup(ExplorerUtils.createLookup(manager, getActionMap()));
        manager.setRootContext(new RootNode());
    }

    private void initComponents() {
        BeanTreeView treeView = new ClickableBeanTreeView();
        setLayout(new java.awt.BorderLayout());
        add(treeView, java.awt.BorderLayout.CENTER);
        treeView.setRootVisible(false);
    }

    @Override
    public void componentOpened() {
    
    }

    @Override
    public void componentClosed() {
    
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    /**
     * Provides BeanTreeView that has pupup menu on mouse right click.
     */
    private class ClickableBeanTreeView extends BeanTreeView {

        public ClickableBeanTreeView() {
            super();
            tree.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger() && manager.getSelectedNodes().length == 0) {
                        JPopupMenu contextMenu = new JPopupMenu();
                        contextMenu.add(new CreateClassificatorAction());
                        contextMenu.add(new ImportClassificatorAction());
                        contextMenu.show(ClassificatorExplorerTC.this,
                                e.getX(),
                                e.getY()
                        );
                    }

                }
            });
        }

    }
}
