/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Every folder that contais {@code pattern.proj} file is considered to be project root
 * folder.
 * 
 * @author palas
 */
@ServiceProvider(service=ProjectFactory.class)
public class PatternProjectFactory implements ProjectFactory{
    
    /** Basic project file. Recognizer **/
    public static final String PROJECT_FILE = "pattern.proj";

    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROJECT_FILE) != null;
    }

    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new PatternProject(dir, state) : null;
    }

    @Override
    public void saveProject(Project project) throws IOException, ClassCastException {
       PatternProject p = project.getLookup().lookup(PatternProject.class);
       p.getClassificator().save(p.getProjectDirectory().getFileObject(PROJECT_FILE));
    }
    
}
