import java.util.Random;

/**
 * Created by chira on 04.12.2017.
 */
public class RandomForme{

    public int getMx() {
        return mx;
    }

    public int getMy() {
        return my;
    }

    static int mx=1;
    static int my=1;
    Handler handler;
    public RandomForme(Handler handler)
    {
        this.handler=handler;
    }

    public void randomMar()
    {
        int k=0;
        Random random=new Random();
        while (true)
        {
            k=0;
            mx= random.nextInt(899);
            my= random.nextInt(599);
            for(int i=0;i<handler.shape.size();i++)
            {
                if(handler.shape.get(i).getPozx()!=mx || handler.shape.get(i).getPozy()!=my)
                {
                    k++;
                }
            }
            if((mx % 30== 0 && my % 30==0)&& handler.shape.size()==k)break;
        }
    }


}
