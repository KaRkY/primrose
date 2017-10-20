package primrose.immutables.graphql;

import static graphql.Scalars.GraphQLBoolean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLOutputType;

public class PropertyDataFetcher<T> implements DataFetcher<T> {

  private final String propertyName;

  public PropertyDataFetcher(final String propertyName) {
    this.propertyName = propertyName;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T get(final DataFetchingEnvironment environment) {
    final Object source = environment.getSource();
    if (source == null) { return null; }
    if (source instanceof Map) { return (T) ((Map<?, ?>) source).get(propertyName); }
    return (T) getPropertyViaGetter(source, environment.getFieldType());
  }

  /**
   * Invoking public methods on package-protected classes via reflection causes
   * exceptions. This method searches a class's hierarchy for public visibility
   * parent classes with the desired getter. This particular case is required to
   * support AutoValue style data classes, which have abstract public interfaces
   * implemented by package-protected (generated) subclasses.
   */
  private Method findAccessibleMethod(final Class<?> root, final String methodName) throws NoSuchMethodException {
    Class<?> cur = root;
    while (cur != null) {
      if (Modifier.isPublic(cur.getModifiers())) {
        final Method m = cur.getMethod(methodName);
        if (Modifier.isPublic(m.getModifiers())) { return m; }
      }
      cur = cur.getSuperclass();
    }
    // noinspection unchecked
    return root.getMethod(methodName);
  }

  private Object getPropertyViaGetter(final Object object, final GraphQLOutputType outputType) {
    try {
      if (isBooleanProperty(outputType)) {
        try {
          return getPropertyViaGetterUsingPrefix(object, "is");
        } catch (final NoSuchMethodException e) {
          return getPropertyViaGetterUsingPrefix(object, "get");
        }
      } else {
        try {
          return getPropertyViaGetterUsingPrefix(object, "get");
        } catch (final NoSuchMethodException e) {
          return getPropertyViaGetterWithoutPrefix(object);
        }
      }
    } catch (final NoSuchMethodException e1) {
      return getPropertyViaFieldAccess(object);
    }
  }

  private Object getPropertyViaGetterUsingPrefix(final Object object, final String prefix)
    throws NoSuchMethodException {
    final String getterName = prefix + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    try {
      final Method method = findAccessibleMethod(object.getClass(), getterName);
      return method.invoke(object);

    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new GraphQLException(e);
    }
  }

  private Object getPropertyViaGetterWithoutPrefix(final Object object) throws NoSuchMethodException {
    try {
      final Method method = findAccessibleMethod(object.getClass(), propertyName);
      return method.invoke(object);

    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new GraphQLException(e);
    }
  }

  private boolean isBooleanProperty(final GraphQLOutputType outputType) {
    if (outputType == GraphQLBoolean) { return true; }
    if (outputType instanceof GraphQLNonNull) { return ((GraphQLNonNull) outputType)
      .getWrappedType() == GraphQLBoolean; }
    return false;
  }

  private Object getPropertyViaFieldAccess(final Object object) {
    try {
      final Field field = object.getClass().getField(propertyName);
      return field.get(object);
    } catch (final NoSuchFieldException e) {
      return null;
    } catch (final IllegalAccessException e) {
      throw new GraphQLException(e);
    }
  }
}
