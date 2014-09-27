package causalreasoningsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO< remove elements from workingset if it is written out ??? >
public class TrackbackGenerator
{
    static public ArrayList<Integer> generate(Random random, DecoratedCausalGraph causalGraph)
    {
        ArrayList<Integer> result;
        
        // list with all indices of nodes which can be added to the output
        // node could also be linked indirectly
        List<Integer> workingNodeIndices;
        
        result = new ArrayList<>();
        
        causalGraph.resetAnnotation();
        
        // add all root indices
        workingNodeIndices = getRootIndices(causalGraph);
        
        for(;;)
        {
            if( workingNodeIndices.size() == 0 )
            {
                break;
            }
            
            // get and remove random node from workingset
            int currentElementIndexIndex = random.nextInt(workingNodeIndices.size());
            int currentElementIndex = workingNodeIndices.get(currentElementIndexIndex);
            workingNodeIndices.remove(currentElementIndexIndex);

            assert !causalGraph.nodes.get(currentElementIndex).anotation.isInOutput();

            // walk all outgoing edges and remove red flag if its there,
            // if there is a redflag count the redflag counter down,
            // if the counter hits zero, add it to the workingNodes
            int outgoingEdgeI;

            for( outgoingEdgeI = 0; outgoingEdgeI < causalGraph.nodes.get(currentElementIndex).outgoingEdgeElementIndices.length; outgoingEdgeI++ )
            {
                boolean redFlagOfEdge;
                int outgoingNodeIndex;

                outgoingNodeIndex = causalGraph.nodes.get(currentElementIndex).outgoingEdgeElementIndices[outgoingEdgeI];

                int incommingEdgeElementIndicesIndex = getIndexOfElementInArray(causalGraph.nodes.get(outgoingNodeIndex).incommingEdgeElementIndices, currentElementIndex);

                redFlagOfEdge = causalGraph.nodes.get(outgoingNodeIndex).anotation.incommingEdgesRedFlags[incommingEdgeElementIndicesIndex];
                if( redFlagOfEdge )
                {
                    assert causalGraph.nodes.get(outgoingNodeIndex).anotation.incommingEdgesRedFlagsCounter > 0;

                    causalGraph.nodes.get(outgoingNodeIndex).anotation.incommingEdgesRedFlagsCounter--;
                    causalGraph.nodes.get(outgoingNodeIndex).anotation.incommingEdgesRedFlags[incommingEdgeElementIndicesIndex] = false;
                    if(
                        causalGraph.nodes.get(outgoingNodeIndex).anotation.incommingEdgesRedFlagsCounter == 0 &&
                        !causalGraph.nodes.get(outgoingNodeIndex).anotation.isOrWasInWorkingSet
                    )
                    {
                        causalGraph.nodes.get(outgoingNodeIndex).anotation.isOrWasInWorkingSet = true;

                        // add it to the workingNodes
                        workingNodeIndices.add(new Integer(outgoingNodeIndex));
                    }
                }
            }




            int outputIndex = result.size();
            causalGraph.nodes.get(currentElementIndex).anotation.outputIndex = outputIndex;
            result.add(new Integer(currentElementIndex));



            // repeat for all outgoing edges of the current element:
            //    mark all incomming edges of the outgoing nodes as red if they don't point at an element which is allready in the output

            for( outgoingEdgeI = 0; outgoingEdgeI < causalGraph.nodes.get(currentElementIndex).outgoingEdgeElementIndices.length; outgoingEdgeI++ )
            {
                int outgoingNodeIndex;
                int incommingEdgeI;

                outgoingNodeIndex = causalGraph.nodes.get(currentElementIndex).outgoingEdgeElementIndices[outgoingEdgeI];

                for( incommingEdgeI = 0; incommingEdgeI < causalGraph.nodes.get(outgoingNodeIndex).incommingEdgeElementIndices.length; incommingEdgeI++ )
                {
                    int reflectedNodeIndex;

                    reflectedNodeIndex = causalGraph.nodes.get(outgoingNodeIndex).incommingEdgeElementIndices[incommingEdgeI];

                    if( causalGraph.nodes.get(reflectedNodeIndex).anotation.isInOutput() )
                    {
                        continue;
                    }

                    causalGraph.nodes.get(outgoingNodeIndex).anotation.incommingEdgesRedFlags[incommingEdgeI] = true;
                }

                causalGraph.nodes.get(outgoingNodeIndex).anotation.recountIncommingRedFlags();
            }
            
            // now we add outgoing nodes if they don't have any red flaged incomming edges
            // (means that all nodes which point at that node are allready in the output)
            
            for( outgoingEdgeI = 0; outgoingEdgeI < causalGraph.nodes.get(currentElementIndex).outgoingEdgeElementIndices.length; outgoingEdgeI++ )
            {
                int outgoingNodeIndex;
                int incommingEdgeI;

                outgoingNodeIndex = causalGraph.nodes.get(currentElementIndex).outgoingEdgeElementIndices[outgoingEdgeI];
                
                if(
                    causalGraph.nodes.get(outgoingNodeIndex).anotation.incommingEdgesRedFlagsCounter == 0 &&
                    !causalGraph.nodes.get(outgoingNodeIndex).anotation.isOrWasInWorkingSet
                )
                {
                    causalGraph.nodes.get(outgoingNodeIndex).anotation.isOrWasInWorkingSet = true;
                    
                    workingNodeIndices.add(new Integer(outgoingNodeIndex));
                }
            }
            
        }
        
        // we must have the same amount of nodes in the graph and nodes in the result
        assert result.size() == causalGraph.nodes.size();
        
        return result;
        
        /*
        bounceFrom(rootIndicesIndex, causalGraph);
        
        int currentNodeIndex = rootIndicesIndex;
        
        boolean existsAnyRemainingOutgoingEdgeBounced = causalGraph.nodes.get(currentNodeIndex).anotation.existAnyRemainingOutgoingEdgeBounced();
        if( !existsAnyRemainingOutgoingEdgeBounced )
        {
            // TODO< add following node of red egde and continue algorithm >
        }
        else
        {
            DecoratedCausalGraph.Node.Anotation.OutgoingEdge currentOutgoingEdge;
            
            currentOutgoingEdge = causalGraph.nodes.get(currentNodeIndex).anotation.getRandomOutgoingEdgeWithRemainingOutgoingEdgeBounced(random);
            
            // choose and remove random bounced edge and add it to the result
            // TODO<  >
            
            // TODO< add the node to the traversingNodes >
        }
        */
        
        // TODO
        
        //return result;
    }
    
    // TODO< what do we about the red connection? >
    
    /**
     * 
     * goes to all outgoing nodes and links in bounced connections 
     * 
     *
     */
    /*
    static private void bounceFrom(int nodeIndex, DecoratedCausalGraph causalGraph) {
        DecoratedCausalGraph.Node selectedNode;
        
        selectedNode = causalGraph.nodes.get(nodeIndex);
        
        **
         * 
         * iterate trough each outgoing connection of this node and anotate the outgoing path with the nodes which should follow before the red nodes get inserted
         * 
         *
        for( int outgoingEdgeIndex = 0; outgoingEdgeIndex < selectedNode.outgoingEdgeElementIndices.length; outgoingEdgeIndex++ )
        {
            DecoratedCausalGraph.Node outgoingNode;
            int iterationOutgoingIndex;
            
            iterationOutgoingIndex = selectedNode.outgoingEdgeElementIndices[outgoingEdgeIndex];
            
            outgoingNode = causalGraph.nodes.get(iterationOutgoingIndex);
            
            outgoingNode.anotation.outgoingEdges[outgoingEdgeIndex].isRed = true;
            
            for( int iterationBouncedIndex : outgoingNode.incommingEdgeElementIndices )
            {
                if( iterationBouncedIndex == iterationOutgoingIndex )
                {
                    // we don't want to include the actual incomming path in our bounced indices for that node
                    continue;
                }
                
                if( causalGraph.nodes.get(iterationBouncedIndex).anotation.inOutput )
                {
                    // we are not interested in bounced noes which are allready in the output
                    continue;
                }
                
                selectedNode.anotation.outgoingEdges[outgoingEdgeIndex].outgoingEdgeBounced.add(new Integer(iterationBouncedIndex));
            }
        }
    }*/
    
    static private ArrayList<Integer> getRootIndices(DecoratedCausalGraph causalGraph)
    {
        ArrayList<Integer> result;
        int i;
        
        result = new ArrayList<Integer>();
        
        i = 0;
        for( DecoratedCausalGraph.Node iterationNode : causalGraph.nodes )
        {
            if( iterationNode.isRoot() )
            {
                result.add(new Integer(i));
            }
            
            i++;
        }
        
        return result;
    }
    
    static private int getIndexOfElementInArray(int[] array, int element)
    {
        int i;
        
        for( i = 0; i < array.length; i++ )
        {
            if( array[i] == element )
            {
                return i;
            }
        }
        
        throw new RuntimeException();
    }
}
