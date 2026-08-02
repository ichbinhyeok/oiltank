package owner.buriedoiltank.heating;

import java.math.BigDecimal;

public record HeatingOilPricePoint(String areaId, String areaName, BigDecimal dollarsPerGallon) {
}
