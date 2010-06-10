package org.seamoo.daos.twigImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.vercer.engine.persist.annotation.AnnotationStrategy;
import com.vercer.engine.persist.strategy.CombinedStrategy;

public class EagerAnnotationStrategy extends AnnotationStrategy {

	public EagerAnnotationStrategy(boolean indexPropertiesDefault, int defaultVersion) {
		super(indexPropertiesDefault, defaultVersion);
		// TODO Auto-generated constructor stub
	}
	
	public int activationDepth(Field field, int depth)
	{
		return depth;
	}


}
