package org.strem;

public class DOgImpl implements DogInt{
    @Override
    public boolean test(Dog d) {
        if (d.getAge()>10)
        {
            return true;
        }
        return  false;
    }
}
