package main.rice.node;
import main.rice.obj.APyObj;
import main.rice.obj.PyFloatObj;
import main.rice.obj.PyBoolObj;
import java.util.*;

import java.util.HashSet;
import java.util.Set;

// TODO: implement the PyFloatNode class here
public class PyFloatNode extends APyNode<PyFloatObj>{
    /**
     * Generates and returns all valid Python objects of type ObjType within the exhaustive domain.
     * @return a set of PyObjs of type PyFloatObj within the exhaustive domain.
     */
    @Override
    public Set<PyFloatObj> genExVals() {
        // create the set we want to return
        Set<PyFloatObj> resultSet = new HashSet<>();
        // iterate over exhaustive domain
        for (Number num: this.getExDomain()){
            resultSet.add(new PyFloatObj(num.doubleValue()));
        }
        return resultSet;
    }
    /**
     * Generates and returns one valid Python object of type ObjType, selected from the random domain.
     * @return one valid Python object of type PyFloatObj, selected from the random domain.
     */
    @Override
    public PyFloatObj genRandVal() {
        int domainSize = this.getRanDomain().size();
        // create an random instance
        Random rando = new Random();
        int index = rando.nextInt(domainSize);
        Number elem = this.getRanDomain().get(index);
        return new PyFloatObj(elem.doubleValue());
    }
}