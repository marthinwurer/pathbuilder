package Players.BHM3791;

/**
 * Created by Benjamin on 2/27/2015. Taken from http://mcts.ai/code/java.html and modified for my own uses.
 */
import java.util.*;

public class TreeNode {

    static Random rand = new Random();
    static int mainPlayer = -1;
    static double epsilon = 1e-6;
    static int numPlayers = 2;
    static int numPlayouts = 3;
    static double timeLimit = 9e9;
    static int gameLimit = 20000;

    ArrayList<TreeNode> children;
    Board state;
    MyMove initial;
    double nVisits, totValue;
    int player;

    /**
     * constructor for root node.
     *
     * @param mainPlayer - the player who we want to win
     * @param numPlayers - the number of players in the game
     */
    public TreeNode(int mainPlayer, int numPlayers) {

        TreeNode.mainPlayer = mainPlayer;
        this.player = 0;
        TreeNode.numPlayers = numPlayers;
        this.state = new Board(numPlayers);
    }

    /**
     * Constructor for leaf nodes.
     *
     * @param player - the player whose move to calculate
     * @param initial - the move to make
     * @param state - the board state
     */
    public TreeNode(int player, MyMove initial, Board state) {
        this.player = player;
        this.initial = initial;
        this.state = state;
        if( initial != null){
            this.state.update(initial);
        }
    }

    public void selectAction() {
        List<TreeNode> visited = new LinkedList<>();
        TreeNode cur = this;
        visited.add(this);
        while (!cur.isLeaf()) {
            cur = cur.select();
            visited.add(cur);
        }

        // if the current node is a final state, backpropogate and return
        if (state.winner() != -1) {
            int winner = state.winner();
            double value = 0.0;
            if (winner == mainPlayer) {
                value = 1.0;
            }
            for (TreeNode node : visited) {
                // would need extra logic for n-player game
                node.updateStats(value);
                node.updateStats(value);
                node.updateStats(value);
            }
            return;
        }

        // otherwise generate and move on.
        cur.expand();
        TreeNode newNode = cur.select();
        visited.add(newNode);

        // evaluate three games.
        for (int ii = 0; ii < numPlayouts; ii++) {
            double value = rollOut(newNode);
            for (TreeNode node : visited) {
                // would need extra logic for n-player game
                node.updateStats(value);
            }
        }
    }

    public void expand() {
        // allocate memory for the next level
        List<MyMove> moves = state.allMoves(player);
//        state.pruneMoves(moves);
        children = new ArrayList<>(moves.size());
        int nextPlayer = (player + 1) % numPlayers;
        for (MyMove move : moves) {
            children.add(new TreeNode(nextPlayer, move, new Board(state)));
        }
//        for( TreeNode node: children){
//            if( node.initial.type == 0){
//                if( nextPlayer == mainPlayer){
//                    node.totValue -= 1;
//                }else{
//                    node.totValue += 1;
//                }
//
//            }
//        }
    }

    private TreeNode select() {
        TreeNode selected = null;
        double bestValue = Double.MIN_VALUE;
        for (TreeNode c : children) {
            double uctValue = c.UCTVal(this) + rand.nextDouble() * epsilon;
            //System.out.println(uctValue);
            // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public double rollOut(TreeNode tn) {
        // ultimately a roll out will end in some value
        // assume for now that it ends in a win or a loss
        // and just return this at random

        // copy the state and then run a random game.
        Board copy = new Board(tn.state);
//        int winner = copy.playRandomGame(rand, tn.player);
//        if (winner == mainPlayer) {
//            return 1.0;
//        } else {
            return 0;
//        }
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }

    public double UCTVal() {
        if (player != mainPlayer) {
            return totValue / (nVisits + epsilon)
                    + Math.sqrt(Math.log(nVisits + 1) / (nVisits + epsilon)) * .5;
        } else {
            return (1 - (totValue / (nVisits + epsilon)))
                    + Math.sqrt(Math.log(nVisits + 1) / (nVisits + epsilon)) * .5;
        }
    }

    public double UCTVal( TreeNode parent) {
        if (player != mainPlayer) {
            return totValue / (nVisits + epsilon)
                    + Math.sqrt(Math.log(parent.nVisits + 1) / (nVisits + epsilon)) * .5;
        } else {
            return (1 - (totValue / (nVisits + epsilon)))
                    + Math.sqrt(Math.log(parent.nVisits + 1) / (nVisits + epsilon)) * .5;
        }
    }

//    public int arity() {
//        return children == null ? 0 : children.size();
//    }

    public MyMove nextMove() {

        // sort thee children by their visits.
        Collections.sort(children, new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode o1, TreeNode o2) {

                if (o1.nVisits > o2.nVisits) {
                    return -1;
                } else if (o1.nVisits < o2.nVisits) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        MyMove best = null;
        double most = -1;
        if (!isLeaf()) {
            for (TreeNode c : children) {
                if (c.nVisits > most) {
                    most = c.nVisits;
                    best = c.initial;
                }
            }
        }
        return best;
    }

    public TreeNode nextNode() {
        TreeNode best = null;
        double most = -1;
        if (!isLeaf()) {
            for (TreeNode c : children) {
                if (c.nVisits > most) {
                    most = c.nVisits;
                    best = c;
                }
            }
        }
        return best;
    }

    public void sortProb(){
        Collections.sort(children, new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode o1, TreeNode o2) {

                if (o1.totValue / (o1.nVisits + epsilon) > o2.totValue / (o2.nVisits + epsilon)) {
                    return -1;
                } else if (o1.totValue / (o1.nVisits + epsilon) < o2.totValue / (o2.nVisits + epsilon)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    public String toString() {
        return "AI.Move: " + initial + " Visits: " + nVisits + /*" UCT: " + this.UCTVal() + */
                " Player: " + player + " Probability: " + totValue/nVisits;
    }

    public void calcNextMove(){

        long time = System.nanoTime();
        int trials = 0;
        while( System.nanoTime() - time < timeLimit && trials < gameLimit) {
            trials ++;
            this.selectAction();
        }
    }

    public TreeNode getNode( MyMove move){

        if( children != null) {
            for (TreeNode node : children) {
                if (node.initial.equals(move)) {
                    return node;
                }
            }
        }

        // if no node is found, return a new treenode with the current
        // boardstate modified given the new move.
        TreeNode toReturn = new TreeNode((player + 1) % numPlayers, move, new Board(state));

        System.out.println(toReturn.state);

        return toReturn;
    }
}