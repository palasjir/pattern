/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.*;
import org.openide.actions.NewAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.pattern.api.ClassificatorController;
import org.pattern.data.Classificator;
import org.pattern.project.PatternProject;

/**
 *
 * @author palas
 */
public class ClassificatorNode extends BeanNode<Classificator> implements ClassificatorController.Listener{
    
    @StaticResource
    private static final String ICON = "org/pattern/classification/ui/resources/ic_classificator.png";
    
    private Classificator classificator;
    private PatternProject project;
    
    public ClassificatorNode(PatternProject bean) throws IntrospectionException{
        this(bean, new InstanceContent());
    }
    
    private ClassificatorNode(PatternProject bean, InstanceContent ic) throws IntrospectionException{
        super(bean.getClassificator(), Children.create(new LabelsChildFactory(bean.getClassificator()), true), 
                new AbstractLookup(ic));
        this.classificator = bean.getClassificator();
        //ClassificatorController.getInstance().addChangeListener(this);
        project = bean;
        ic.add(classificator);
        setDisplayName(classificator.getInfo().getId());
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? super Action> actions = new ArrayList<>();
        actions.add(SystemAction.get(NewAction.class));
        List<? extends Action> classificatorActions
                = Utilities.actionsForPath("Actions/Classificator");
        actions.addAll(classificatorActions);
        return actions.toArray(new Action[classificatorActions.size()]);
    }

    @Override
    public NewType[] getNewTypes() {
        return new NewType[]{
            new NewType() {

                @Override
                public String getName() {
                    return "label";
                }

                @Override
                public void create() throws IOException {
                    NotifyDescriptor.InputLine msg
                            = new NotifyDescriptor.InputLine(
                                    "Name",
                                    "New label");
                    DialogDisplayer.getDefault().notify(msg);
                    if(msg.getValue().equals(NotifyDescriptor.OK_OPTION)){
                        classificator.createLabel(msg.getInputText());
                    }
                }
            }
        };
    }
    
    @Override
    public String getHtmlDisplayName() {
        if(classificator != null){
            return "<font color='!textText'>" + classificator.getInfo().getId() + "</font>" +
                " <font color='!controlShadow'><i>[" + project.getProjectDirectory().getName() + "]</i></font>";
        }else{
            return null;
        }
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(ICON);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
    
    

    @Override
    public void onClassificatorCreated(Classificator classificator) {
    }

    @Override
    public void onMainClassificatorChanged(Classificator oldMain, Classificator newMain) {
        fireDisplayNameChange("", getDisplayName());
    }
}
