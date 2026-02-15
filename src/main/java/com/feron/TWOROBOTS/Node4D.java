package com.feron.TWOROBOTS;

import java.util.ArrayList;
import java.util.List;

public class Node4D {
    private Node4D parent;  
    private double x1, y1, x2, y2;
    private double dist;
    private List<Node4D> children; 

    public Node4D (double x1, double y1, double x2, double y2){
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        this.parent = null;
        this.dist = 0.0;
        this.children = new ArrayList<Node4D>();
    }
    
    public Node4D(){}

    public void set_parent(Node4D parent){
        if (this.parent != null){
            this.parent.remove_child(this);
        }
        this.parent = parent;
        if (this.parent != null){
            this.parent.add_child(this);
            this.dist = parent.dist + Math.sqrt(this.dist_sq(parent));
        }
    }

    public Node4D get_parent() { return this.parent; }
    public double get_x1() { return this.x1; }
    public double get_y1() { return this.y1; }
    public double get_x2() { return this.x2; }
    public double get_y2() { return this.y2; }
    public double get_dist() { return this.dist; }
    
    public double dist_sq(Node4D other) {
        return (this.x1 - other.x1)*(this.x1 - other.x1) + (this.y1 - other.y1)*(this.y1 - other.y1) +
               (this.x2 - other.x2)*(this.x2 - other.x2) + (this.y2 - other.y2)*(this.y2 - other.y2);
    }

    public List<Node4D> get_children(){ return this.children; }
    public void add_child(Node4D n){ this.children.add(n); }
    public void remove_child(Node4D n){ this.children.remove(n); }

    public void propagatecost(){
        for (Node4D child : this.children){
            child.dist = this.dist + Math.sqrt(this.dist_sq(child));
            child.propagatecost();
        }
    }
}