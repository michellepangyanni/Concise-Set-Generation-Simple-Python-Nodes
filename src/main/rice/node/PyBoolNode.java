package main.rice.node;
import main.rice.obj.APyObj;
import main.rice.obj.PyBoolObj;
import java.util.*;
// TODO: implement the PyBoolNode class here
public class PyBoolNode extends APyNode<PyBoolObj>{
    /**
     * Generates and returns all valid Python objects of type ObjType within the exhaustive domain.
     * @return a set of PyObjs of type PyBoolObj within the exhaustive domain.
     */
    @Override
    public Set<PyBoolObj> genExVals() {
        // create the set we want to return
        Set<PyBoolObj> resultSet = new HashSet<>();
        // iterate through exhaustive domain
        for (Object num: this.getExDomain()){
            if ((int) num == 0){
                resultSet.add(new PyBoolObj(false));
            }

            resultSet.add(new PyBoolObj(true));
        }
        return resultSet;
    }

    /**
     * Generates and returns one valid Python object of type ObjType, selected from the random domain.
     * @return one valid Python object of type PyBoolObj, selected from the random domain.
     */
    @Override
    public PyBoolObj genRandVal() {
        if (getRanDomain().size() ==1){
            if ((int)getRanDomain().get(0) == 0){
                return new PyBoolObj(false);
            }
            return new PyBoolObj(true);
        }
        // create an random instance
        Random rando = new Random();
        boolean nextBool =rando.nextBoolean();
        return new PyBoolObj(nextBool);

    }
}