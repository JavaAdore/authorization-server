package com.eltaieb.microservice.base.footprint;

import java.io.Serializable;

public interface Referenceable extends Serializable {

	String getReference();
}
