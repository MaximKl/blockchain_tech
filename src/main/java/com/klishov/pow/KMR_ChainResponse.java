package com.klishov.pow;

import java.util.List;

public record KMR_ChainResponse(List<KMR_Block> chain, int length) {
}
