package org.kpi.fict.pzks.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;

import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultElement;
import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultElement.ResultElementType;
import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultStructure;
import org.kpi.fict.pzks.tree.blocks.Block;
import org.kpi.fict.pzks.tree.blocks.Block.BlockType;
import org.kpi.fict.pzks.tree.blocks.CompoundBlock;
import org.kpi.fict.pzks.tree.blocks.CompoundBlock.BlockSignType;
import org.kpi.fict.pzks.tree.blocks.ConstantBlock;
import org.kpi.fict.pzks.tree.blocks.SignBlock;
import org.kpi.fict.pzks.tree.blocks.SignBlock.SignType;
import org.kpi.fict.pzks.tree.blocks.VariableBlock;


/**
 * Utility class which builds tree from automate result structure.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class TreeBuilder {
    // Singleton
    private static TreeBuilder singletonInstance;

    private TreeBuilder() { }

    public static TreeBuilder getInstance() {
        return (singletonInstance == null) ? (singletonInstance = new TreeBuilder()) :
            singletonInstance;
    }


    /**
     * Maim utility method which builds tree of binary blocks. 
     * 
     * @param res The result structure (built by automate).
     * @return Result block (we can draw this block.) 
     */
    public ArrayList<Block> buildTree(ResultStructure res) {
        Block block; 

        block = transformStructToBlocks(res);

        block = normaliseBlocks(block);

        block = applyInvert(block);
        
        if (!block.isCompound()) {
            ArrayList<Block> r = new ArrayList<Block>();
            r.add(block);
            return r;
        }

        ArrayList<Block> variants = trySortBlock(block);
        
        block = variants.get(0);

        return variants;
    }

    public Block applyInvert(Block block) {
        if (!block.isCompound()) return block;

        try {
            CompoundBlock compBlock = (CompoundBlock) block;

            ArrayList<Block> invertedBlocks = new ArrayList<Block>();

            if (compBlock.isInverted()) {
                if (compBlock.getBlockSignType() == BlockSignType.MULDIV) {
                    // if this is */ block,  so we should invert only first block

                    Block first = (Block) compBlock.get(0).clone();
                    first.invert();
                    invertedBlocks.add(applyInvert(first));
                    for (int i = 1; i < compBlock.size(); i++) {
                        invertedBlocks.add(compBlock.get(i));
                    }
                } else {
                    // if this is +- block, so we should invert first block and all signs
                    Block firstBlock = (Block) compBlock.get(0).clone();
                    firstBlock.invert();                    
                    invertedBlocks.add(applyInvert(firstBlock));

                    for (int i = 1; i < compBlock.size(); i++) {
                        if (compBlock.get(i).isSign()) {
                            invertedBlocks.add(((SignBlock) compBlock.get(i)).getInvertedSign());
                        } else {
                            invertedBlocks.add(applyInvert(compBlock.get(i)));
                        }
                    }
                }
            } else {
                for (Block b : compBlock) {
                    invertedBlocks.add(applyInvert(b));
                }
            }
            
            CompoundBlock resBlock = new CompoundBlock(invertedBlocks);
            return resBlock;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * =====================SORT====================== 
     * // TODO:
     * 
     * @param block
     */
    private static class SortPair {
        SignBlock sign;
        Block block;
        int weight;
        
        public SortPair(SignBlock sign, Block block) {
            this.sign = sign;
            this.block = block;
            weight = blockWeight(block);
        }
        
        @Override
        public String toString() {
            return ((sign == null) ? "^" : sign.toString()) + " " + block.toString() + " || W = " + weight + "\n";
        }
    }
    
    private ArrayList<Block> trySortBlock(Block block) {
        CompoundBlock compBlock = (CompoundBlock) block;

        ArrayList<SortPair> sortPairs = new ArrayList<SortPair>();

        ArrayList<Block> sortedBlocks = new ArrayList<Block>();
        for (Block b : compBlock) {
            sortedBlocks.add(simpleSort(b));
        }
        compBlock = new CompoundBlock(sortedBlocks);
        
        SortPair firstPair = null;
        if (compBlock.getBlockSignType() == BlockSignType.MULDIV) {
            firstPair = new SortPair(new SignBlock(SignType.MUL), compBlock.get(0));
        } else {
            firstPair = new SortPair(new SignBlock(SignType.PLUS), compBlock.get(0));
        }
        
        sortPairs.add(firstPair);
        
        int i = 1;
        while (i < compBlock.size()) {
            Block sign = compBlock.get(i);
            Block b = compBlock.get(i + 1);
            i = i + 2;
            
            sortPairs.add(new SortPair(((SignBlock) sign), b));
        }
        
//        Collections.sort(sortPairs, new Comparator<SortPair>() {
//            public int compare(SortPair o1, SortPair o2) {
//                return o1.weight - o2.weight;
//            }
//        });
        
        TreeMap<Integer, ArrayList<SortPair>> categories = new TreeMap<Integer, ArrayList<SortPair>>();
        
        for (SortPair pair : sortPairs) {
            ArrayList<SortPair> category = null;
            if (!categories.containsKey(pair.weight)) {
                category = new ArrayList<TreeBuilder.SortPair>();
                categories.put(pair.weight, category);
            } else {
                category = categories.get(pair.weight);
            }
            category.add(pair);
        }
        
        // find candidate for first position
        for (ArrayList<SortPair> categ : categories.values()) {
            for (SortPair p : categ) {
                if (!p.sign.isDivision()) {
                    firstPair = p;
                    categ.remove(firstPair);
                    if (categ.size() == 0) 
                        categories.remove(firstPair.weight);
                    break;
                }
            }
            break;
        }
        
        if (firstPair.sign.isMinus()) {
            firstPair.block.invert();
        }
        
        ArrayList<Block> subblocks = new ArrayList<Block>();
        subblocks.add(firstPair.block);
        ArrayList<Block> resultSet = new ArrayList<Block>();
        
        if (categories.size() == 1 && categories.containsKey(0)) {
            for (SortPair p : categories.get(0)) {
                subblocks.add(p.sign);
                subblocks.add(p.block);
            }
            resultSet.add(new CompoundBlock(subblocks));
        } else {
            makeExpression(resultSet, new ArrayList<ArrayList<SortPair>>(categories.values()), subblocks);
        }
        
        System.out.println(resultSet);
        
        return resultSet;//resBlock;
    }

    private Block simpleSort(Block block) {
        if (!block.isCompound()) return block;
        CompoundBlock compBlock = (CompoundBlock) block;

        ArrayList<SortPair> sortPairs = new ArrayList<SortPair>();

        SortPair firstPair = null;
        if (compBlock.getBlockSignType() == BlockSignType.MULDIV) {
            firstPair = new SortPair(new SignBlock(SignType.MUL), compBlock.get(0));
        } else {
            firstPair = new SortPair(new SignBlock(SignType.PLUS), compBlock.get(0));
        }
        
        sortPairs.add(firstPair);
        
        int i = 1;
        while (i < compBlock.size()) {
            Block sign = compBlock.get(i);
            Block b = compBlock.get(i + 1);
            i = i + 2;
            
            sortPairs.add(new SortPair(((SignBlock) sign), b));
        }
        
//        Collections.sort(sortPairs, new Comparator<SortPair>() {
//            public int compare(SortPair o1, SortPair o2) {
//                return o1.weight - o2.weight;
//            }
//        });
        
        TreeMap<Integer, ArrayList<SortPair>> categories = new TreeMap<Integer, ArrayList<SortPair>>();
        
        for (SortPair pair : sortPairs) {
            ArrayList<SortPair> category = null;
            if (!categories.containsKey(pair.weight)) {
                category = new ArrayList<TreeBuilder.SortPair>();
                categories.put(pair.weight, category);
            } else {
                category = categories.get(pair.weight);
            }
            category.add(pair);
        }
        
        // find candidate for first position
        for (ArrayList<SortPair> categ : categories.values()) {
            for (SortPair p : categ) {
                if (!p.sign.isDivision()) {
                    firstPair = p;
                    categ.remove(firstPair);
                    if (categ.size() == 0) 
                        categories.remove(firstPair.weight);
                    break;
                }
            }
            break;
        }
        
        if (firstPair.sign.isMinus()) {
            firstPair.block.invert();
        }
        
        ArrayList<Block> subblocks = new ArrayList<Block>();
        subblocks.add(firstPair.block);
        ArrayList<Block> resultSet = new ArrayList<Block>();
        
        if (categories.size() == 1 && categories.containsKey(0)) {
            for (SortPair p : categories.get(0)) {
                subblocks.add(p.sign);
                subblocks.add(p.block);
            }
            resultSet.add(new CompoundBlock(subblocks));
        } else {
            makeExpression(resultSet, new ArrayList<ArrayList<SortPair>>(categories.values()), subblocks);
        }
        
        System.out.println(resultSet);
        
        return resultSet.get(0);//resBlock;
    }
    
    private void makeExpression(
            ArrayList<Block> resultSet, 
            List<ArrayList<SortPair>> categories, 
            ArrayList<Block> subblocks) {
        if (categories.size() == 0) {
            resultSet.add(new CompoundBlock(subblocks));
            return;
        }
        
        ArrayList<ArrayList<Block>> variants = new ArrayList<ArrayList<Block>>();
        ArrayList<Block> expression = new ArrayList<Block>();
        makeCombinations(categories.get(0), expression, variants);
        
        for (ArrayList<Block> var : variants) {
            ArrayList<Block> subB = new ArrayList<Block>();
            for (Block b : subblocks) {
                subB.add(b);
            }
            
            subB.addAll(var);

            makeExpression(resultSet, categories.subList(1, categories.size()), subB);
        }
    } 
    
    private void makeCombinations(ArrayList<SortPair> availible, ArrayList<Block> expression, ArrayList<ArrayList<Block>> solutions) {
        if (availible.size() == 0) {
            solutions.add(expression);
        }
        
        for (SortPair pair : availible) {
            ArrayList<SortPair> newAvail = new ArrayList<TreeBuilder.SortPair>(); 
            for (SortPair p : availible) {
                if (p != pair) newAvail.add(p);
            }
            
            ArrayList<Block> newExpression = new ArrayList<Block>();
            for (Block b : expression) {
                newExpression.add(b);
            }
            newExpression.add(pair.sign);
            newExpression.add(pair.block);
            
            makeCombinations(newAvail, newExpression, solutions);
        }
    }
    
    /**
     * Block weight. // TODO:
     * 
     * Weight of:
     * <ul>
     * <li>Compound - sum weight of subblocks</li>
     * <li>Constant - 0</li>
     * <li>Variable - 0</li>
     * <li>"+" - 1<li> 
     * <li>"-" - 1</li>
     * <li>"*" - 2</li>
     * <li>"/" - 3</li>
     * <li></li>
     * </ul>
     * 
     * @param block The input block.
     * @return Overall weight of block.
     */
    private static int blockWeight(Block block) {
        if (!block.isCompound()) {
            if (block.isSign()) {
                SignBlock sign = (SignBlock) block;

                switch (sign.getSignType()) {
                case DIV:   return 3;
                case MUL:   return 2;
                default:    return 1;
                }
            } else {
                return 0;
            }
        } else {
            CompoundBlock compBlock = (CompoundBlock) block;
            int sumWeight = 0;

            for (Block b : compBlock) {
                sumWeight += blockWeight(b);
            }

            return sumWeight;
        }
    }

    /**
     * Create block from input result structure. All brackets replaced by subblocks.
     * 
     * @param inStruct input structure (created by automate).
     * @return block
     */
    private Block transformStructToBlocks(ResultStructure inStruct) {
        if (inStruct.size() == 1) {
            // if structure contains only one element (it must be constant or variable)
            switch (inStruct.get(0).getType()) {
            case CONSTANT: return new ConstantBlock(inStruct.get(0));
            case VARIABLE: return new VariableBlock(inStruct.get(0));
            }
        } else {
            // remove outside brackets
            while (inStruct.firstElement().getType() == ResultElementType.OPEN_BRACKET && 
                    inStruct.lastElement().getType() == ResultElementType.CLOSE_BRACKET) 
            {
                ResultElement first = inStruct.get(0);
                ResultElement last = inStruct.get(inStruct.size() - 1);

                inStruct.remove(0);
                inStruct.remove(inStruct.size() - 1);

                // Protection of case: (a+b) * (a+b)
                if (!bracketBalance(inStruct)) {
                    inStruct.add(0, first);
                    inStruct.add(last);
                    break;
                }
            }

            ArrayList<Block> blocks = new ArrayList<Block>();
            ListIterator<ResultElement> inStructIterator = inStruct.listIterator();

            while (inStructIterator.hasNext()) {
                ResultElement curElem = inStructIterator.next();
                Block curBlock = null;

                switch (curElem.getType()) {
                case VARIABLE:      curBlock = new VariableBlock(curElem);  break;
                case CONSTANT:      curBlock = new ConstantBlock(curElem);  break;
                case SIGN:          curBlock = new SignBlock(curElem);     break;
                case INVERTOR:      
                    inStructIterator.next();
                    curBlock = transformStructToBlocks(formBracketSubstructure(inStructIterator));
                    curBlock.invert();
                    break;
                case OPEN_BRACKET:  curBlock = transformStructToBlocks(formBracketSubstructure(inStructIterator));
                }

                blocks.add(curBlock);
            }

            // if block contains only one elementary sublock - return this elementary block
            if (blocks.size() == 1) return blocks.get(0);

            return new CompoundBlock(blocks);
        }
        return null;
    }

    /**
     * Test bracket balance.
     * 
     * @param struct The input structure.
     * @return If bracket balance is balanced (:
     */
    private boolean bracketBalance(ResultStructure struct) {
        int count = 0;
        for (ResultElement elem : struct) {
            if (elem.getType() == ResultElementType.OPEN_BRACKET) count++;
            if (elem.getType() == ResultElementType.CLOSE_BRACKET) count--;
            if (count < 0) return false;
        }
        return true;
    }

    /**
     * Separation of bracket substructure "[SIGN] (&lt; This part is separated &gt;) [SIGN]"
     * 
     * @param inStructIter
     * @return
     */
    private ResultStructure formBracketSubstructure(ListIterator<ResultElement> inStructIter) {
        ResultStructure res = new ResultStructure();
        res.addElement(new ResultElement(ResultElementType.OPEN_BRACKET, "("));
        int bracketCount = 1;

        while (bracketCount > 0) {
            ResultElement curElem = inStructIter.next();
            res.addElement(curElem);
            if (curElem.getType() == ResultElementType.OPEN_BRACKET) bracketCount++;
            if (curElem.getType() == ResultElementType.CLOSE_BRACKET) bracketCount--;
        }

        return res;
    }

    /**
     * After this method on every level we have operations of one priority (or - and +, or * and /). 
     * 
     * @param headBlock head block of structure
     * @return block with grouped components
     */
    private Block normaliseBlocks(Block headBlock) {
        if (headBlock.getBlockType() != BlockType.COMPOUND) {
            return headBlock;
        } else {
            CompoundBlock headComBlock = (CompoundBlock) headBlock;

            // if there at least one + or -
            boolean minplusPresent = false;
            for (Block block : headComBlock) {
                if (block.getBlockType() == BlockType.SIGN) {
                    if ( (((SignBlock) block).getSignType() == SignType.PLUS) ||
                            (((SignBlock) block).getSignType() == SignType.MINUS) ) {
                        minplusPresent = true;
                        break;
                    }
                }
            }

            ArrayList<Block> resBlocks = new ArrayList<Block>();
            if (minplusPresent) {
                ArrayList<Block> tempBlocks = new ArrayList<Block>();
                for (Block block : headComBlock) {
                    BlockType type = block.getBlockType();

                    if (type == BlockType.SIGN &&
                            ( ((SignBlock) block).getSignType() == SignType.PLUS ||
                            ((SignBlock) block).getSignType() == SignType.MINUS) )
                    {
                        if (tempBlocks.size() == 1) {
                            resBlocks.add(normaliseBlocks(tempBlocks.get(0)));
                        } else {
                            resBlocks.add(normaliseBlocks(new CompoundBlock(tempBlocks)));
                        }

                        resBlocks.add(block);
                        tempBlocks = new ArrayList<Block>();
                    } else {
                        tempBlocks.add(block);
                    }
                }

                if (tempBlocks.size() == 1) {
                    resBlocks.add(normaliseBlocks(tempBlocks.get(0)));
                } else {
                    resBlocks.add(normaliseBlocks(new CompoundBlock(tempBlocks)));
                }
            } else {
                for (Block block : headComBlock) {
                    resBlocks.add(normaliseBlocks(block));
                }
            }

            return new CompoundBlock(resBlocks, headBlock.isInverted());
        }
    }

    /**
     * Makes binary blocks: A + B + C + D = ((A + B) + (C + D))
     * 
     * @param block The input block.
     * @return The result block.
     */
    public Block makeBinaryBlocks(Block block) {
        if (block.getBlockType() != BlockType.COMPOUND) {
            return block;
        } else {
            CompoundBlock compBlock = (CompoundBlock) block;

            ArrayList<Block> processedBlocks = new ArrayList<Block>();

            // process all subblocks
            for (Block subblock : compBlock) {
                processedBlocks.add(makeBinaryBlocks(subblock));
            }

            ArrayList<Block> prevBlocks = processedBlocks;
            ArrayList<Block> currentBlocks = prevBlocks;

            // if we have in the block situation like: (subblock sign subblock) - mission completed
            while (prevBlocks.size() > 3) {
                currentBlocks = new ArrayList<Block>();

                // count number of notSign blocks
                while (prevBlocks.size() > 0) {
                    int notSingBlockCount = notSignBlocksCount(prevBlocks);

                    if (notSingBlockCount > 1) {
                        Block [] curBlocksArr = (Block[]) prevBlocks.toArray(new Block[0]);

                        // if first lock is SIGN - we select 4 blocks: [Sign B Sign B]
                        // else - we select first 3  blocks: [B Sign B]
                        int copyCount = (prevBlocks.get(0).getBlockType() == BlockType.SIGN) ? 4 : 3;

                        Block [] subblocks = Arrays.copyOf(curBlocksArr, copyCount);
                        ArrayList<Block> tempBlocks = makeSubblock(subblocks);

                        // Make choice - make block from current block and next or current and previous
                        Block temp;
                        if (tempBlocks.size() > 1) {
                            temp = tempBlocks.get(1);
                        } else {
                            temp = tempBlocks.get(0);
                        }

                        if (currentBlocks.size() == 0 || 
                                blockWeight(temp) < blockWeight(currentBlocks.get(currentBlocks.size() - 1)) + 1) {
                            currentBlocks.addAll(tempBlocks);
                        } else {
                            currentBlocks.addAll(prevBlocks.subList(0, copyCount));
                        }

                        //currentBlocks.addAll(tempBlocks);

                        for (int i = 0; i < copyCount; i++) {
                            prevBlocks.remove(0);
                        }
                    } else {
                        currentBlocks.addAll(prevBlocks);
                        int addedBlocksCount = prevBlocks.size();
                        for (int i = 0; i < addedBlocksCount; i++) {
                            prevBlocks.remove(0);
                        }

                    }
                }

                prevBlocks = currentBlocks;
            }

            return new CompoundBlock(currentBlocks, block.isInverted());
        }
    }

    /**
     * Counts not sign subblocks in list.
     * 
     * @param blocks The input list.
     * @return Number of not sign blocks in list.
     */
    private int notSignBlocksCount(List<Block> blocks) {
        int res = 0;

        for (Block block : blocks) {
            if (!block.isSign()) res++;
        }

        return res;
    }

    /**
     * Make subbloks:<br>
     * B SIGN B = B<br>
     * SIGN B SIGN B = SIGN B:<br>
     *  + B SIGN B = + B<br>
     *  - B SIGN B = - (B INVERTED SIGN B) = -B<br>
     *  <br>
     *  * B SIGN B = * B<br>
     *  / B SIGN B = / (B INVERTED SIGN B) = /B<br>
     * 
     * @param blocks The input block.
     * @return The result block with binary subblocks.
     */
    private ArrayList<Block> makeSubblock(Block [] blocks) {
        ArrayList<Block> res = null;

        // B SIGN B
        if (blocks.length == 3) {
            res = new ArrayList<Block>();
            res.add(new CompoundBlock(blocks));
        } else {
            //  + B SIGN B = + B
            //  - B SIGN B = - (B INVERTED SIGN B) = -B

            if (blocks.length == 4) {
                res = new ArrayList<Block>();

                // add first sign
                res.add(blocks[0]);

                CompoundBlock resBlock = new CompoundBlock();
                resBlock.addBlock(blocks[1]);

                //make the brackets
                if ( ((SignBlock) blocks[0]).getSignType() == SignType.MINUS ||
                        ((SignBlock) blocks[0]).getSignType() == SignType.DIV) 
                {
                    resBlock.addBlock( ((SignBlock) blocks[2]).getInvertedSign());
                } else {
                    resBlock.addBlock(blocks[2]);
                }

                resBlock.addBlock(blocks[3]);

                res.add(resBlock);
            }
        }

        return res;
    }
    
    public static int treeDeep(Block b) {
        if (!b.isCompound()) return 0;
        CompoundBlock compBlock = (CompoundBlock) b;
        
        int max = 0;
        
        for (Block bb : compBlock) {
            int w = treeDeep(bb);
            max = (max < w) ? w : max;
        }
        
        return max + 1;
    }
}