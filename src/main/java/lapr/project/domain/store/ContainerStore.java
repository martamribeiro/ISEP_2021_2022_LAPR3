package lapr.project.domain.store;


import lapr.project.domain.model.Container;

import java.util.ArrayList;
import java.util.List;

public class ContainerStore {

    private List<Container> containersList = new ArrayList<>();

    public boolean saveContainer(Container container){
        if(!validateContainer(container)){
            return false;
        }
        return containersList.add(container);
    }

    public boolean validateContainer(Container container){
        return !containersList.contains(container);
    }

    public Container getContainerById(int find){
        for (Container container : containersList){
            if (container.getId() == find){
                return container;
            }
        }
        throw new UnsupportedOperationException("Could not find container with given id");
    }

    public List<Container> getContainersList() {
        return containersList;
    }
}
