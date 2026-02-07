package com.feron.RRT;

import java.util.ArrayList;
import java.util.List;

import com.feron.Visualisation.MyPath;
import com.feron.Visualisation.Point;

public class Tree {
    private Node node;
    private Tree left;
    private Tree right;

    public Tree(Node node){
        this.node=node;
        this.left=null;
        this.right=null;
    }

    public Tree get_left(){
        return this.left;
    }

    public Tree get_right(){
        return this.right;
    }

    public Node get_node(){
        return this.node;
    }

    public void set_left(Tree t){
        this.left=t;
    }
    public void set_right(Tree t){
        this.right=t;
    }

    public void insert (Node n,Tree current, int depth){
        if (current == null || n == null) return;
        if (depth%2==0){
            if (n.get_point().get_x()<current.get_node().get_point().get_x()){
                if (current.get_left()==null){
                    current.set_left(new Tree(n));
                }else{
                    insert(n,current.get_left(),depth+1);
                }
            }else{
                if (current.get_right()==null){
                    current.set_right(new Tree(n));
                }else{
                    insert (n,current.get_right(),depth+1);
                }
            }
        }else{
            if (n.get_point().get_y()<current.get_node().get_point().get_y()){
                if (current.get_left()==null){
                    current.set_left(new Tree(n));
                }else{
                    insert(n,current.get_left(),depth+1);
                }
            }else {
                if (current.get_right()==null){
                    current.set_right(new Tree(n));
                }else{
                insert (n,current.get_right(),depth+1);
                }
            }
        }
    }

    private Node findNearest_rec(Point p, int depth,Tree current,Node best){
        if (current == null) return best;
        double current_dist=p.dist_sq(current.get_node().get_point());
        double best_dist=p.dist_sq(best.get_point());

        if (current_dist<best_dist){
            best_dist=current_dist;
            best=current.get_node();
        }

        int dim =depth%2;
        double diff= dim==0 ? p.get_x()-current.get_node().get_point().get_x():
                              p.get_y()-current.get_node().get_point().get_y();
        
        Tree nearChild = (diff < 0) ? current.get_left() : current.get_right();
        Tree farChild = (diff < 0) ? current.get_right() : current.get_left();
        
        best=findNearest_rec(p, depth+1, nearChild, best);
        if (diff*diff<p.dist_sq(best.get_point())){
            best=findNearest_rec(p, depth+1, farChild, best);
        }
        return best;
    }

    public Node findNearest(Point p){
        if (this.node==null){
            return null;
        }else{
            return findNearest_rec(p, 0, this, this.node);
        }
    }

    private List<Node> neighbours_rec(Point p, double radius,Tree current, List<Node> neighbours,int depth){
        if (current == null || current.node == null) {
            return neighbours;
        }else{
            double current_dist=p.dist_sq(current.get_node().get_point());
            if (current_dist<radius*radius){
                neighbours.add(current.node);
            }
        int dim =depth%2;
        double diff= dim==0 ? p.get_x()-current.node.get_point().get_x():
                              p.get_y()-current.node.get_point().get_y();
        
        Tree nearChild = (diff < 0) ? current.left : current.right;
        Tree farChild = (diff < 0) ? current.right : current.left;
        
        neighbours=neighbours_rec(p,radius,nearChild,neighbours,depth+1);
        if (diff*diff<radius*radius){
            neighbours=neighbours_rec(p, radius, farChild, neighbours,depth+1);
        }
        return neighbours;
        }
    }

    public List<Node> neighbour_in_radius(Point p,double radius){
        List<Node> neighbours=new ArrayList<Node>();
        return neighbours_rec(p,radius,this,neighbours,0);
    }

    public boolean contain_node(Node n){
        if(n.get_point().dist_sq(findNearest(n.get_point()).get_point())>0.0){
            return false;
        }else{
            return true;
        }
    }

    public MyPath path_to_start(Node n){
        if (n==null){
            return new MyPath();
        }
            MyPath path=new MyPath();
            Node current=n;
            while (current!=null){
                path.get_points().add(current.get_point().get_y());
                path.get_points().add(current.get_point().get_x());
                current=current.get_parent();
            }
            return path;
        }

    @Override
    public Tree clone(){
        if (this.node==null){
            return null;
        }else {
            Tree t=new Tree(this.node);
            t.left=this.left.clone();
            t.right=this.right.clone();
            return t;
        }

    }
 
}
