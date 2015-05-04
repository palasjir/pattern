/**
 * *****************************************************************************
 * Copyright 2013 Lars Behnke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package org.patern.clustering.hierarchical;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Cluster {

    public Object obj;
    
    private String name;
    private Cluster parent;
    private List<Cluster> children;
    private Distance distance = new Distance();
    
    protected Color color = Color.BLACK;
    
    public Cluster(String name){
        this.name = name;
    }
    
    public Cluster(Object obj) {
        this.obj = obj;
        this.name = String.valueOf(obj.toString());
    }
    
    public Cluster(Object obj, String name){
        this.obj = obj;
        this.name = name;
    }

    public Distance getDistance() {
        return distance;
    }

    public Double getWeightValue() {
        return distance.getWeight();
    }

    public Double getDistanceValue() {
        return distance.getDistance();
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public List<Cluster> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<Cluster> children) {
        this.children = children;
    }

    public Cluster getParent() {
        return parent;
    }

    public void setParent(Cluster parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void addChild(Cluster cluster) {
        getChildren().add(cluster);
    }

    public boolean contains(Cluster cluster) {
        return getChildren().contains(cluster);
    }

    @Override
    public String toString() {
        return "Cluster " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Cluster other = (Cluster) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (name == null) ? 0 : name.hashCode();
    }

    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    public int countLeafs() {
        return countLeafs(this, 0);
    }

    public int countLeafs(Cluster node, int count) {
        if (node.isLeaf()) {
            count++;
        }
        for (Cluster child : node.getChildren()) {
            count += child.countLeafs();
        }
        return count;
    }
    
    public void getLeafs(List<Cluster> leafs){
        if(isLeaf()){
            leafs.add(this);
        }else{
            for (Cluster child : children) {
                child.getLeafs(leafs);
            }
        }
    }

    public void toConsole(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");

        }
        String str = getName() + (isLeaf() ? " (leaf)" : "") + (distance != null ? "  distance: " + distance : "");
        System.out.println(str);
        for (Cluster child : getChildren()) {
            child.toConsole(indent + 1);
        }
    }

    public double getTotalDistance() {
        Double dist = getDistance() == null ? 0 : getDistance().getDistance();
        if (getChildren().size() > 0) {
            dist += children.get(0).getTotalDistance();
        }
        return dist;
    }
    
    /**
     * Splits dendrogram into multiple clusters by cutting distance.
     * 
     * @param cut
     * @param found clusters from cut
     * @author palas
     */
    public void makeCut(double cut, List<Cluster> found){
        if(cut < distance.getDistance()){
            for (Cluster child : children) {
                if(!child.isLeaf()){
                    child.makeCut(cut, found);
                }else{
                    found.add(child);
                }
            }
        }else{
            found.add(this);
        }
    }

    public void setColorRecursively(Color color) {
        setColor(color);
        for (Cluster child : children) {
            child.setColorRecursively(color);
        }
    }

}
