/*
 * File created on Mar 11, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.prospecto.jaxrs.runtime.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.api.url.UrlResolver;
import org.soulwing.prospecto.jaxrs.api.AmbiguousPathResolutionException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceConfigurationException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;
import org.soulwing.prospecto.jaxrs.runtime.ResourceNotFoundException;
import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;

/**
 * A simple {@link UrlResolver} backed by a set of resource descriptors.
 *
 * @author Carl Harris
 */
public class ResourceDescriptorUrlResolver implements ConfigurableUrlResolver {

  private static final Logger logger =       
      LoggerFactory.getLogger(ResourceDescriptorUrlResolver.class);
  
  private final Set<ResourceDescriptor> descriptors =
      new HashSet<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void addAll(Collection<ResourceDescriptor> descriptors) {
    for (ResourceDescriptor descriptor : descriptors) {
      addDescriptor(descriptor);
    }
  }

  public void addDescriptor(ResourceDescriptor descriptor) {
    logger.debug("{}", descriptor);
    descriptors.add(descriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validate() throws ResourceConfigurationException {
    if (findDuplicatedPaths(createPathList())) {
      throw new ResourceConfigurationException(
          "found duplicate resource descriptors");
    }
  }

  private boolean findDuplicatedPaths(List<ModelPath> paths) {
    Set<ModelPath> pathSet = new HashSet<>(paths.size());
    pathSet.addAll(paths);
    if (pathSet.size() == paths.size()) return false;

    Map<ModelPath, Set<ResourceDescriptor>> pathMap = new HashMap<>();
    for (ResourceDescriptor descriptor : descriptors) {
      final ModelPath modelPath = descriptor.modelPath();
      Set<ResourceDescriptor> descriptorSet = pathMap.get(modelPath);
      if (descriptorSet == null) {
        descriptorSet = new HashSet<>();
        pathMap.put(modelPath, descriptorSet);
      }
      descriptorSet.add(descriptor);
    }

    for (ModelPath path : pathMap.keySet()) {
      final Set<ResourceDescriptor> descriptors = pathMap.get(path);
      if (descriptors.size() <= 1) continue;
      for (ResourceDescriptor descriptor : descriptors) {
        logger.error("DUPLICATE: {}", descriptor);
      }
    }

    return true;
  }

  private List<ModelPath> createPathList() {
    List<ModelPath> paths = new LinkedList<>();
    for (ResourceDescriptor descriptor : descriptors) {
      paths.add(descriptor.modelPath());
    }
    return paths;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String resolve(ViewNode node, ViewContext context) {
    final ResourceDescriptor descriptor = findBestMatch(
        ModelPath.with(context.currentModelPath()));
    final String urlTemplate =
        context.getUrlDecorators().decorate(descriptor.path(), node, context);
    return descriptor.templateResolver().resolve(urlTemplate, context);
  }

  private ResourceDescriptor findBestMatch(ModelPath modelPath) {
    List<ResourceDescriptor> matches = findAllMatches(modelPath);

    final int numMatches = matches.size();
    if (numMatches == 0) {
      throw new ResourceNotFoundException(modelPath);
    }
    if (numMatches == 1) {
      final ResourceDescriptor descriptor = matches.get(0);
      if (logger.isTraceEnabled()) {
        logger.trace("{} has singular match {}", modelPath, descriptor);
      }
      return descriptor;
    }

    final ResourceDescriptor exactMatch = findExactMatch(modelPath, matches);
    if (exactMatch != null) {
      if (logger.isTraceEnabled()) {
        logger.trace("{} has exact match {}", modelPath, exactMatch);
      }
      return exactMatch;
    }

    matches = findLongestMatches(modelPath, matches);
    final int length = matches.get(0).modelPath().length();
    int step = 0;
    while (step < length && matches.size() > 1) {
      matches = findBestMatchesAtStep(step++, modelPath, matches);
    }
    if (matches.size() > 1) {
      throw new AmbiguousPathResolutionException(modelPath, matches);
    }

    return matches.get(0);
  }

  private ResourceDescriptor findExactMatch(ModelPath modelPath,
      List<ResourceDescriptor> allMatches) {
    assert allMatches.size() > 0;
    if (allMatches.size() == 1) {
      return allMatches.get(0);
    }

    List<ResourceDescriptor> matches = new ArrayList<>();
    for (ResourceDescriptor descriptor : allMatches) {
      if (descriptor.modelPath().equals(modelPath)) {
        matches.add(descriptor);
      }
    }

    final int numMatches = matches.size();
    if (numMatches == 0) {
      return null;
    }

    if (numMatches > 1) {
      throw new AmbiguousPathResolutionException(modelPath, matches);
    }

    return matches.get(0);
  }

  private List<ResourceDescriptor> findBestMatchesAtStep(int step,
      ModelPath modelPath, List<ResourceDescriptor> descriptors) {
    ModelPath.MatchType matchType = bestMatchTypeAtStep(step, descriptors);
    List<ResourceDescriptor> matches = new ArrayList<>(descriptors.size());
    for (ResourceDescriptor descriptor : descriptors) {
      if (descriptor.modelPath().matchTypeAt(step) == matchType) {
        if (logger.isTraceEnabled()) {
          logger.trace("at step {}: {} has best match {}", step, modelPath,
              descriptor);
        }

        matches.add(descriptor);
      }
    }
    return matches;
  }

  private ModelPath.MatchType bestMatchTypeAtStep(int step,
      List<ResourceDescriptor> descriptors) {
    assert descriptors.size() >= 1;
    ModelPath.MatchType bestMatchType = descriptors.get(0).modelPath()
        .matchTypeAt(step);
    for (int i = 1, max = descriptors.size(); i < max; i++) {
      ModelPath.MatchType matchType = descriptors.get(i).modelPath()
          .matchTypeAt(step);
      if (matchType.ordinal() < bestMatchType.ordinal()) {
        bestMatchType = matchType;
      }
    }
    return bestMatchType;
  }

  private List<ResourceDescriptor> findLongestMatches(
      ModelPath modelPath, List<ResourceDescriptor> allMatches) {
    if (allMatches.size() <= 1) return allMatches;
    Collections.sort(allMatches, new Comparator<ResourceDescriptor>() {
      @Override
      public int compare(ResourceDescriptor a, ResourceDescriptor b) {
        return b.modelPath().length() - a.modelPath().length();
      }
    });

    int longest = allMatches.get(0).modelPath().length();
    List<ResourceDescriptor> longestMatches = new ArrayList<>();
    int i = 0;
    while (i < allMatches.size()
        && allMatches.get(i).modelPath().length() == longest) {
      final ResourceDescriptor descriptor = allMatches.get(i++);
      if (logger.isTraceEnabled()) {
        logger.trace("{} has longest match {}", modelPath, descriptor);
      }
      longestMatches.add(descriptor);
    }
    return longestMatches;
  }

  private List<ResourceDescriptor> findAllMatches(ModelPath modelPath) {
    List<ResourceDescriptor> matches = new ArrayList<>();
    for (ResourceDescriptor descriptor : descriptors) {
      if (descriptor.matches(modelPath)) {
        logger.trace("{} matches {}", modelPath, descriptor);
        matches.add(descriptor);
      }
    }
    return matches;
  }

}
