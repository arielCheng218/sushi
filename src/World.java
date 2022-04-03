import java.util.ArrayList;

public class World {

  public ArrayList<Object> objects = new ArrayList<Object>();
  public int time;

  public void addObject(Object obj) {
    objects.add(obj);
  }
}