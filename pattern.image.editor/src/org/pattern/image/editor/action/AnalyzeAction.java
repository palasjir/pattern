/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.pattern.api.analysis.AnalysisProvider;
import org.pattern.project.MultiImageDataObject;

@ActionID(
        category = "Image",
        id = "org.pattern.editor.action.AnalyzeAction"
)
@ActionRegistration(
        displayName = "#CTL_AnalyzeAction",
        iconBase = "org/pattern/image/editor/resources/ic_analyze.png"
)
@ActionReferences({
    @ActionReference(path = "Menu/Image", position = 500, separatorAfter = 501),
    @ActionReference(path = "Toolbars/Image", position = 200)
})
@Messages("CTL_AnalyzeAction=Analyze")
public final class AnalyzeAction implements ActionListener {
    
    private final MultiImageDataObject context;
    
    public AnalyzeAction(MultiImageDataObject image){
        this.context = image;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // FIXME can be more of them
        AnalysisProvider provider = Lookup.getDefault().lookup(AnalysisProvider.class);
        if(provider != null){
            provider.openAnalysis(context);
        }
    }
}
