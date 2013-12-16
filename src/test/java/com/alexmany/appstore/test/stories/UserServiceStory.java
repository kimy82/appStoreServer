package com.alexmany.appstore.test.stories;

import java.util.Arrays;
import java.util.List;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.spring.UsingSpring;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.spring.SpringAnnotatedEmbedderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringAnnotatedEmbedderRunner.class)
@Configure()
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true)
@UsingSpring(resources = { "/com/alexmany/appstore/test/spring/steps.xml" })
public class UserServiceStory extends InjectableEmbedder {

	@Test
	public void run() {
		injectedEmbedder().runStoriesAsPaths(storyPaths());
	}

	protected List<String> storyPaths() {
		 return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
	                Arrays.asList("**/" + System.getProperty("storyFilter", "*") + ".story"), null);
	}

}
