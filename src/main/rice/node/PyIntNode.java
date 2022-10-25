package main.rice.node;
import main.rice.obj.APyObj;
import main.rice.obj.PyIntObj;
import main.rice.obj.PyFloatObj;
import java.util.*;
// TODO: implement the PyIntNode class here
public class PyIntNode extends APyNode<PyIntObj>{
    /**
     * Generates and returns all valid Python objects of type PyIntObj within the exhaustive domain.
     * @return a set of PyObjs of type PyIntObj within the exhaustive domain.
     */
    @Override
    public Set<PyIntObj> genExVals() {
        // create the set we want to return
        Set<PyIntObj> resultSet = new HashSet<>();
        // iterate over exhaustive domain
        for(Number num: this.getExDomain()){
            resultSet.add(new PyIntObj(num.intValue()));
        }
        return resultSet;
    }
    /**
     * Generates and returns one valid Python object of type PyIntObj, selected from the random domain.
     * @return one valid Python object of type PyIntObj, selected from the random domain.
     */
    @Override
    public PyIntObj genRandVal() {
        int domainSize = this.getRanDomain().size();
        // create random instance
        Random rando = new Random();
        int index = rando.nextInt(domainSize);
        Number elem = this.getRanDomain().get(index);
        return new PyIntObj(elem.intValue());
    }
}