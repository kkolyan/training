/**
 * This code contains copyright information which is the proprietary property
 * of SITA Information Network Computing Limited (SITA). No part of this
 * code may be reproduced, stored or transmitted in any form without the prior
 * written permission of SITA.
 *
 * Copyright (C) SITA Information Network Computing Limited 2009-2012.
 * All rights reserved.
 */
package net.kkolyan.spring.altimpl;

import java.lang.reflect.Type;

/**
 * @author nplekhanov
 */
public interface Transformer {
    Object transform(Type genericType, Object o) throws Exception;
}
