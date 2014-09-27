package causalreasoningsystem;

import java.util.ArrayList;
import java.util.Random;

public class DecoratedCausalGraph
{
    public static class Node
    {
        public static class Anotation
        {
            /*
            public class OutgoingEdge
            {
                public boolean isRed;
                
                public ArrayList<Integer> outgoingEdgeBounced = new ArrayList<Integer>();
                // invariant: elements in that list are not allready in the output
                // elements get removed as elements are taken out
                
                public boolean areRemainingOutgoingEdgeBounced()
                {
                    return outgoingEdgeBounced.size() > 0;
                }
            }
            
            public OutgoingEdge[] outgoingEdges;
            */
            
            public boolean isOrWasInWorkingSet; // is/was this node in the working set for ttraversal?
            
            // flags used to indicate that the node of the incomming edge must occur before that node
            // flag is not set for elements which are allready in the output
            public boolean[] incommingEdgesRedFlags;
            
            public int incommingEdgesRedFlagsCounter;
            
            public void recountIncommingRedFlags()
            {
                int incommingEdgeI;
                
                incommingEdgesRedFlagsCounter = 0;
                
                for( incommingEdgeI = 0; incommingEdgeI < incommingEdgesRedFlags.length; incommingEdgeI++ )
                {
                    if( incommingEdgesRedFlags[incommingEdgeI] )
                    {
                        incommingEdgesRedFlagsCounter++;
                    }
                }
            }
            
            public int outputIndex = -1;
            
            public boolean isInOutput()
            {
                return outputIndex != -1;
            }
            
            /*
            public boolean existAnyRemainingOutgoingEdgeBounced()
            {
                int i;
                
                for( i = 0; i < outgoingEdges.length; i++ )
                {
                    if( outgoingEdges[i].areRemainingOutgoingEdgeBounced() )
                    {
                        return true;
                    }
                }
                
                return false;
            }
            
            // INVARIANT< only callable if existAnyRemainingOutgoingEdgeBounced() returns true, else it goes into an infinite loop >
            public OutgoingEdge getRandomOutgoingEdgeWithRemainingOutgoingEdgeBounced(Random random)
            {
                for(;;)
                {
                    int currentOutgoingEdgeIndex;
                    
                    currentOutgoingEdgeIndex = random.nextInt(outgoingEdges.length);
                    
                    if( outgoingEdges[currentOutgoingEdgeIndex].areRemainingOutgoingEdgeBounced() )
                    {
                        return outgoingEdges[currentOutgoingEdgeIndex];
                    }
                }
            }*/
        }
        
        public Anotation anotation = new Anotation();
        
        public int[] outgoingEdgeElementIndices;
        public int[] incommingEdgeElementIndices;
        
        public boolean isRoot()
        {
            return incommingEdgeElementIndices.length == 0;
        }
    }
    
    public ArrayList<Node> nodes = new ArrayList<Node>();
    
    //public int[] rootIndices;
    
    public void resetAnnotation()
    {
        int i;
        
        for( i = 0; i < nodes.size(); i++ )
        {
            nodes.get(i).anotation = new Node.Anotation();
            
            int incommingEdgesArrayLength = nodes.get(i).incommingEdgeElementIndices.length;
            nodes.get(i).anotation.incommingEdgesRedFlags = new boolean[incommingEdgesArrayLength];
        }
    }
}
