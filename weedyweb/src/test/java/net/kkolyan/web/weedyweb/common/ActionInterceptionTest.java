package net.kkolyan.web.weedyweb.common;

import net.kkolyan.web.weedyweb.api.ActionInterceptor;
import net.kkolyan.web.weedyweb.api.InterceptionPoint;
import net.kkolyan.web.weedyweb.api.ModelAndView;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:nplekhanov86@gmail.com">nplekhanov</a>
 */
public class ActionInterceptionTest {
    @Test
    public void init() throws Exception {
        final List<String> seq = new ArrayList<String>();
        List<ActionInterceptor> list = new ArrayList<ActionInterceptor>();
        list.add(new ActionInterceptor() {
            @Override
            public ModelAndView intercept(InterceptionPoint interceptionPoint) throws Exception {
                try {
                    seq.add("A");
                    return interceptionPoint.executeAction();
                } finally {
                    seq.add("G");
                }
            }
        });
        list.add(new ActionInterceptor() {
            @Override
            public ModelAndView intercept(InterceptionPoint interceptionPoint) throws Exception {
                try {
                    seq.add("B");
                    return interceptionPoint.executeAction();
                } finally {
                    seq.add("F");
                }
            }
        });
        list.add(new ActionInterceptor() {
            @Override
            public ModelAndView intercept(InterceptionPoint interceptionPoint) throws Exception {
                try {
                    seq.add("C");
                    return interceptionPoint.executeAction();
                } finally {
                    seq.add("E");
                }
            }
        });
        ActionInterception.executeViaInterceptors(list, new InterceptionPoint() {
            @Override
            public ModelAndView executeAction() {
                seq.add("D");
                return null;
            }
        });
        System.out.println(seq);
        Assert.assertArrayEquals(new Object[] {"A","B","C","D","E","F","G"}, seq.toArray());
    }
}
