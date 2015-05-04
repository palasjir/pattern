package org.pattern.api.analysis;

import org.pattern.data.ParticleImage;

/**
 * Represents analysis performed on image.
 * 
 * @author palasjiri
 */
public interface Analysis {
    
    /**
     * Getter of creation time.
     * @return time when was analysis created.
     */
    public Long getTime();
    
    /**
     * Getter of execution time.
     * @return time of last execution time, null if no execution was performed yet
     */
    public Long getRunTime();
    
    /**
     * Getter of analysis type.
     */
    public void getType();
    
    /**
     * Getter of analysis result
     * @return analysis result, null if no execution was performed yet
     */
    public Object getResult();
    
    /**
     * Getter of analysis title.
     * @return 
     */
    public String getTitle();
    
    /**
     * Set analysis title
     * @param text new title
     */
    public void setTitle(String text);
    
    /**
     * Gets execution state of analysis.
     * @return true if analysis was already executed at least once, false otherwise
     */
    public boolean isExecuted();
    
    /**
     * Runs analysis on given data.
     * @param data single slice data.
     */
    public void execute(ParticleImage data);
}
