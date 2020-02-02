package main.heaps;

public class HeapNode{

    public int currentCount;
    private ItemNode[] items;

    public HeapNode(int maxCount){
        items = new ItemNode[maxCount];
    }

    public void addItem(ItemNode item){
        items[currentCount] = item;
        item.index = currentCount;
        sortUp(item);
        item.node.openItem = item;
        currentCount++;
    }

    public ItemNode removeFirstItem(){
        ItemNode first = items[0];
        currentCount--;
        items[0] = items[currentCount];
        items[0].index = 0;
        sortDown(items[0]);
        return first;
    }

    public void updateItem(ItemNode item){
        sortUp(item);
        item.node.openItem = item;
    }

    public boolean contains(ItemNode item){
        return (items[item.index] == item);
    }

    private void sortDown(ItemNode item){
        while(true){
            int childIndexA = item.index*2+1;
            int childIndexB = item.index*2+2;
            int swapIndex = 0;
            if (childIndexA < currentCount){
                swapIndex = childIndexA;
                if (childIndexB < currentCount){
                    if (items[childIndexA].value > items[childIndexB].value){
                        swapIndex = childIndexB;
                    }
                }
                if (item.value > items[swapIndex].value){
                    swap(item,items[swapIndex]);
                }
                else{
                    return;
                }
            }
            else{
                return;
            }
        }
    }

    private void sortUp(ItemNode item){
        int parentIndex = (item.index-1)/2;
        while(true) {
            ItemNode parent = items[parentIndex];
            if (item.value < parent.value){
                swap(item,parent);
            }
            else{
                break;
            }
            parentIndex = (item.index-1)/2;
        }
    }
    private void swap(ItemNode a, ItemNode b){
        items[a.index] = b;
        items[b.index] = a;
        int aIndex = a.index;
        a.index = b.index;
        b.index = aIndex;
    }
}
