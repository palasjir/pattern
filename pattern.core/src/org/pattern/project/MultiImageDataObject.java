/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import java.io.IOException;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.pattern.data.MultiImage;

@Messages({
    "LBL_Dummy_LOADER=Files of MultiImage"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_Dummy_LOADER",
        mimeType = "text/x-pattern",
        extension = {"pattern"}
)
@DataObject.Registration(
        mimeType = "text/x-pattern",
        iconBase = "org/pattern/resources/ic_particles.png",
        displayName = "#LBL_Dummy_LOADER",
        position = 300
)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/x-pattern/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 400
    ),
    @ActionReference(
            path = "Loaders/text/x-pattern/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.SaveAction"),
            position = 300
    ),
    @ActionReference(
            path = "Loaders/text/x-pattern/Actions",
            id = @ActionID(category = "Image", id = "org.pattern.project.action.AnalyzeAction"),
            position = 400,
            separatorAfter = 500
    ),
    @ActionReference(
            path = "Loaders/text/x-pattern/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600
    )
//    @ActionReference(
//            path = "Loaders/text/x-pattern/Actions",
//            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
//            position = 1400
//    )
})
public class MultiImageDataObject extends MultiDataObject {
    
    private MultiImage image = null;
    private FileObject imageFile;
    
    public MultiImageDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-pattern", true);
        getCookieSet().add(new Saver());
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    public Lookup getLookup() {
        return super.getLookup();
    }
    
    @Override
    protected Node createNodeDelegate() {
        return new MultiImageNode(this, getLookup());
    }

    public MultiImage getImage(){
        if(image == null){
            try {
                image = PatternFileSupport.load(getPrimaryFile());
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            getCookieSet().assign(MultiImage.class, image);
        }
        return image;
    }
    
    public PatternProject getOwnerProject(){
        return FileOwnerQuery.getOwner(getPrimaryFile())
                .getLookup().lookup(PatternProject.class);
    }
    
    public FileObject getImageFile(){
        if(imageFile == null){
            imageFile = PatternFileSupport.getImageFileObject(getPrimaryFile());
        }
        return imageFile;
    }
    
    @MultiViewElement.Registration(
            displayName = "#LBL_Dummy_EDITOR",
            iconBase = "org/pattern/resources/ic_particles.png",
            mimeType = "text/x-pattern",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "Dummy",
            position = 3000
    )
    @Messages("LBL_Dummy_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }

    @Override
    protected void handleDelete() throws IOException {
        getImageFile().delete();
        super.handleDelete();
    } 
    
    private class Saver implements SaveCookie{

        @Override
        public void save() throws IOException {
            synchronized(MultiImageDataObject.this){
                PatternFileSupport.savePatternFile(getPrimaryFile(), image);
                setModified(false);
            }
        }
        
    }

}
