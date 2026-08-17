package com.corp.corehyperbolic.domain;

import java.io.Serializable;

public record HyperbolicPoint(
        String nodeIdentifier,
        double[] coordinates,
        double curvatureRadius
) implements Serializable {}
