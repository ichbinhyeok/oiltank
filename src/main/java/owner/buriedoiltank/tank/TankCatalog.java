package owner.buriedoiltank.tank;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TankCatalog {
    private static final LocalDate VERIFIED_ON = LocalDate.of(2026, 8, 1);
    private static final String GRANBY_PRODUCT = "https://www.granbyindustries.com/en-us/petroleum-tanks/products/standard-20-plus/";
    private static final String GRANBY_CHART = "https://www.granbyindustries.com/app/uploads/2026/04/GranbyInd_Capacity-Chart_VerticalTanks_USA_v1.pdf";
    private static final String GRANBY_STANDARD = "https://www.granbyindustries.com/en-us/petroleum-tanks/products/standard/";
    private static final String ROTH_PRODUCT = "https://www.roth-america.com/product/oil-storage-tanks/double-wall-heating-oil-tank/";

    private final List<TankSpec> specs = List.of(
            new TankSpec(
                    "granby-204201p-275-vertical",
                    "Granby",
                    "204201P Standard 20Plus",
                    TankShape.OBROUND,
                    TankOrientation.VERTICAL,
                    275,
                    60,
                    27,
                    44,
                    273.8,
                    granby275VerticalChart(),
                    "Granby Standard 20Plus product table and U.S. vertical capacity chart",
                    GRANBY_CHART,
                    VERIFIED_ON
            ),
            new TankSpec(
                    "granby-204701p-275-horizontal",
                    "Granby",
                    "204701P Standard 20Plus",
                    TankShape.OBROUND,
                    TankOrientation.HORIZONTAL,
                    275,
                    60,
                    44,
                    27,
                    275,
                    List.of(),
                    "Granby Standard 20Plus product table",
                    GRANBY_PRODUCT,
                    VERIFIED_ON
            ),
            new TankSpec(
                    "granby-205201p-330-vertical",
                    "Granby",
                    "205201P Standard 20Plus",
                    TankShape.OBROUND,
                    TankOrientation.VERTICAL,
                    330,
                    72,
                    27,
                    44,
                    328.6,
                    granby330VerticalChart(),
                    "Granby Standard 20Plus product table and U.S. vertical capacity chart",
                    GRANBY_CHART,
                    VERIFIED_ON
            ),
            new TankSpec(
                    "granby-208101-138-vertical",
                    "Granby",
                    "208101 Standard",
                    TankShape.OBROUND,
                    TankOrientation.VERTICAL,
                    138,
                    30,
                    27,
                    44,
                    136.9,
                    granby138VerticalChart(),
                    "Granby Standard residential tank product table and U.S. vertical capacity chart",
                    GRANBY_CHART,
                    VERIFIED_ON
            ),
            new TankSpec(
                    "roth-dwt-1000l-275",
                    "Roth",
                    "DWT 1000L",
                    TankShape.RECTANGULAR,
                    TankOrientation.VERTICAL,
                    275,
                    43,
                    28,
                    61,
                    275,
                    List.of(),
                    "Roth double-wall heating-oil tank dimensions",
                    ROTH_PRODUCT,
                    VERIFIED_ON
            ),
            new TankSpec(
                    "roth-dwt-1000lh-275",
                    "Roth",
                    "DWT 1000LH low height",
                    TankShape.RECTANGULAR,
                    TankOrientation.VERTICAL,
                    275,
                    51,
                    30,
                    54,
                    275,
                    List.of(),
                    "Roth double-wall heating-oil tank dimensions",
                    ROTH_PRODUCT,
                    VERIFIED_ON
            )
    );

    public List<TankSpec> all() {
        return specs;
    }

    public TankSpec require(String id) {
        return specs.stream()
                .filter(spec -> spec.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown tank spec: " + id));
    }

    public List<TankSpec> withGaugeCharts() {
        return specs.stream().filter(TankSpec::hasGaugeChart).toList();
    }

    private static List<GaugePoint> granby275VerticalChart() {
        double[] gallons = {
                0, 1.8, 5.0, 9.0, 13.7, 19.0, 25.1, 31.3, 37.7, 44.3,
                51.2, 58.2, 65.3, 72.4, 79.6, 86.7, 93.9, 101.1, 108.2, 115.4,
                122.6, 129.7, 136.9, 144.1, 151.2, 158.4, 165.6, 172.7, 179.9, 187.1,
                194.3, 201.4, 208.6, 215.6, 222.6, 229.5, 236.1, 242.6, 248.7, 254.4,
                259.8, 264.6, 268.7, 272.0, 273.8
        };
        return chart(gallons);
    }

    private static List<GaugePoint> granby138VerticalChart() {
        return chart(new double[]{
                0, 0.9, 2.5, 4.5, 6.9, 9.5, 12.6, 15.6, 18.8, 22.2,
                25.6, 29.1, 32.6, 36.2, 39.8, 43.4, 47.0, 50.5, 54.1, 57.7,
                61.3, 64.9, 68.5, 72.0, 75.6, 79.2, 82.8, 86.4, 90.0, 93.5,
                97.1, 100.7, 104.3, 107.8, 111.3, 114.7, 118.1, 121.3, 124.3, 127.2,
                129.9, 132.3, 134.4, 136.0, 136.9
        });
    }

    private static List<GaugePoint> granby330VerticalChart() {
        return chart(new double[]{
                0, 2.1, 6.0, 10.8, 16.5, 22.8, 30.2, 37.5, 45.2, 53.2,
                61.4, 69.8, 78.3, 86.9, 95.5, 104.1, 112.7, 121.3, 129.9, 138.5,
                147.1, 155.7, 164.3, 172.9, 181.5, 190.1, 198.7, 207.3, 215.9, 224.5,
                233.1, 241.7, 250.3, 258.8, 267.2, 275.4, 283.4, 291.1, 298.4, 305.3,
                311.7, 317.5, 322.5, 326.4, 328.6
        });
    }

    private static List<GaugePoint> chart(double[] gallons) {
        java.util.ArrayList<GaugePoint> points = new java.util.ArrayList<>();
        for (int inch = 0; inch < gallons.length; inch++) {
            points.add(new GaugePoint(inch, gallons[inch]));
        }
        return List.copyOf(points);
    }
}
