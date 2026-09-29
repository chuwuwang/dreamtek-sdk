package moudles.newModules;

import java.util.ArrayList;

import entity.cases.BaseCase;

/**
 * Created by RuihaoS on 2021/5/9.
 */
public interface Module<T> {
    T setCases(ArrayList<BaseCase>  cases);
}
