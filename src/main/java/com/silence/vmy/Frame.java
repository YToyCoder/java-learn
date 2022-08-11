package com.silence.vmy;

/**
 * <p>runtime frame</>
 * <P>a runtime stack for vmy runtime</P>
 * <P>it store local variable , function</P>
 *
 */
public interface Frame {
  // get local variable from current frame
  Runtime.Variable local(String _name);
}
