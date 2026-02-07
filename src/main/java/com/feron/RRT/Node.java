package com.feron.RRT;

import java.util.ArrayList;
import java.util.List;

import com.feron.Visualisation.Point;

public class Node {
    private Node parent;
    private Point point;
    private double dist;
    private List<Node> children;

    public Node (double x,double y){
        this.point=new Point(x,y);
        this.parent=null;
        this.dist=0.0;
        this.children=new ArrayList<Node>();
    }
    public Node(){}

    public void set_parent(Node parent){
        if (this.parent!=null){
            this.parent.remove_child(this);
        }
        this.parent=parent;
        if (this.parent!=null){
            this.parent.add_child(this);
            this.dist=parent.dist+Math.sqrt(this.get_point().dist_sq(parent.get_point()));
        }
    }

    public Node get_parent(){
        return this.parent;
    }

    public Point get_point(){
        return this.point;
    }

    public double get_dist(){
        return this.dist;
    }

    public List<Node> get_children(){
        return this.children;
    }
    
    public void add_child(Node n){
        this.children.add(n);
    }

    public void remove_child(Node n){
        this.children.remove(n);
    }
    public void propagatecost(){
        for (Node child:this.children){
            child.dist=this.dist+this.get_point().dist_sq(child.get_point());
            child.propagatecost();
        }
    }

}
