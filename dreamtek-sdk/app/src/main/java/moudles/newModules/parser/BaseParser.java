package moudles.newModules.parser;

import java.util.ArrayList;

/**
 * Created by RuihaoS on 2021/5/8.
 */
interface BaseParser<T> {
    ArrayList<T> parse(Class<T> clazz, String path);
}
