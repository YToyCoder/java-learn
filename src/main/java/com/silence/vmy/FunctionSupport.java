package com.silence.vmy;

import java.util.List;

/**
 * all builtin call can get here
 */
public class FunctionSupport {
  private FunctionSupport(){}

  // call a function
  public static Object call(String name, List<Object> params){
    return "has not implement";
  }

  public static final int Builtin = 0;
  public static final int UserDefined = 1;
  public static final int Native = 2;

  public interface FunctionType extends VmyType{
    /**
     * @return types of all params
     */
    List<VmyType> types();

    /**
     * a function's params can see as an array, if you want to get the param of 0 , it can be seen as params[0]
     * @param i
     * @return the type of param in location i, if 'i' is out of length for params return null
     */
    VmyType param_type(int i);

    /**
     * @return Function tag , mark for user defined(user defined using vmy script) , builtin(builtin call) or Native(Write By Java)
     *
     * @see FunctionSupport#Builtin
     * @see FunctionSupport#Native
     * @see FunctionSupport#UserDefined
     */
    int tag();
  }

  /**
   * register a function
   */
  public static interface FunctionRegister {
    void register(String name, FunctionType type, Callable callable);
  }

  /**
   * create a function
   */
  public static interface FunctionFactory {
    /**
     * get a {@link Callable} by function name and function type
     * @param name function name
     * @param type function type
     * @return {@link Callable} or {@code null}
     */
    Callable get_function(String name, FunctionType type);
  }
}
