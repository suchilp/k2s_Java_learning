package org.strem;

public class MyMainAdd {
    public static void main(String[] args) {

        Addition add= new Addition();
        int output=add.add(10,20);
        System.out.println(output);

          Add add1= (a,b)-> a+b;
        System.out.println(add1.sum(10,20));

        DogInt d= new DogInt() {
            @Override
            public boolean test(Dog d) {
                if (d.getAge()>10)
                {
                    return true;
                }
                return  false;
            }
        };

         DogInt d1 =  x -> x.getAge()>10;
        DogInt d2 =  (y) -> y.getAge()>10;
        DogInt d4 =  (y) ->  {return y.getAge()>10;};

        DogInt d5 =  (Dog d9) ->  {return d9.getAge()>10;};


    }
}
