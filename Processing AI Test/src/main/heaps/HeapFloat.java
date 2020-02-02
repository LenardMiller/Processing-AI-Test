package main.heaps;

public class HeapFloat {

    private int currentCount;
    private ItemFloat[] items;

    public HeapFloat(int maxCount){
        items = new ItemFloat[maxCount];
    }

    public void addItem(ItemFloat item){
        items[currentCount] = item;
        item.index = currentCount;
        sortUp(item);
        currentCount++;
    }

    public ItemFloat removeFirstItem(){
        ItemFloat first = items[0];
        currentCount--;
        items[0] = items[currentCount];
        items[0].index = 0;
        sortDown(items[0]);
        return first;
    }

    private void updateItem(ItemFloat item){
        sortUp(item);
    }

    private boolean contains(ItemFloat item){
        return (items[item.index] == item);
    }

    private void sortDown(ItemFloat item){
        while(true){
            int childIndexA = item.index*2+1;
            int childIndexB = item.index*2+2;
            int swapIndex;
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

    private void sortUp(ItemFloat item){
        int parentIndex = (item.index-1)/2;
        while(true) {
            ItemFloat parent = items[parentIndex];
            if (item.value < parent.value){
                swap(item,parent);
            }
            else{
                break;
            }
            parentIndex = (item.index-1)/2;
        }
    }

    private void swap(ItemFloat a, ItemFloat b){
        items[a.index] = b;
        items[b.index] = a;
        int aIndex = a.index;
        a.index = b.index;
        b.index = aIndex;
    }
}
