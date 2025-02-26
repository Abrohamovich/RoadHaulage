//package ua.ithillel.roadhaulage.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import ua.ithillel.roadhaulage.dto.*;
//import ua.ithillel.roadhaulage.entity.OrderStatus;
//import ua.ithillel.roadhaulage.entity.UserRole;
//import ua.ithillel.roadhaulage.service.interfaces.*;
//
//import java.sql.Date;
//import java.util.*;
//
//@Component
//@RequiredArgsConstructor
//public class StartInitializer implements CommandLineRunner {
//    private final UserService userService;
//    private final UserRatingService userRatingService;
//    private final OrderCategoryService orderCategoryService;
//    private final AddressService addressService;
//    private final OrderService orderService;
//
//    @Override
//    public void run(String... args) {
//        adminInit();
//
//        List<UserDto> userDtos = new ArrayList<>();
//        List<AddressDto> addressDtos = new ArrayList<>();
//        List<OrderCategoryDto> orderCategoryDtos = new ArrayList<>();
//
//        testUsersInit().forEach(u -> {
//            userDtos.add(userService.save(u));
//        });
//        testUserRatingInit(userDtos);
//        testOrderCategoryInit().forEach(oc -> {
//            orderCategoryDtos.add(orderCategoryService.save(oc));
//        });
//        testAddressInit().forEach(a -> {
//            addressDtos.add(addressService.save(a));
//        });
//        testOrderInit(userDtos, orderCategoryDtos, addressDtos).forEach(orderService::save);
//    }
//
//    private void adminInit(){
//        String email = System.getenv("ADMIN_EMAIL");
//        Optional<UserDto> userDtoOptional = userService.findByEmail(email);
//        if (userDtoOptional.isEmpty()) {
//            UserDto userDto = new UserDto();
//            userDto.setEmail(email);
//            userDto.setEnabled(true);
//            userDto.setPassword(System.getenv("ADMIN_PASSWORD"));
//            userDto.setPhoneCode(System.getenv("ADMIN_PHONE_CODE"));
//            userDto.setPhone(System.getenv("ADMIN_PHONE_NUMBER"));
//            userDto.setRole(UserRole.ADMIN);
//            userDto.setFirstName("Road");
//            userDto.setLastName("Haulage");
//            userDto = userService.save(userDto);
//
//            UserRatingDto userRatingDto = new UserRatingDto();
//            userRatingDto.setUser(userDto);
//            userRatingDto.setAverage(0.0);
//            userRatingDto.setCount(0);
//            userRatingService.save(userRatingDto);
//        }
//    }
//
//    private List<UserDto> testUsersInit(){
//        return List.of(
//                UserDto.builder().firstName("John").lastName("Doe").phoneCode("33")
//                        .phone("612345678").email("doe.john@gmail.com").role(UserRole.USER)
//                        .enabled(true).iban("FR7630006000011234567890189").password("EAW#$#Co97%#23").build(),
//                UserDto.builder().firstName("Anna").lastName("Schmidt").phoneCode("49")
//                        .phone("15239876543").email("anna.schmidt@gmail.com").role(UserRole.ADMIN)
//                        .enabled(true).iban("DE44100000000123456789").password("QWErTY#1234@xyz").build(),
//
//                UserDto.builder().firstName("Carlos").lastName("Gomez").phoneCode("34")
//                        .phone("678901234").email("carlos.gomez@gmail.com").role(UserRole.USER)
//                        .enabled(true).iban("ES9121000418450200051332").password("mNBvC!98765@#asd").build()
//
//
//        );
//    }
//
//    private void testUserRatingInit(List<UserDto> usersDto){
//        usersDto.forEach(userDto -> {
//            UserRatingDto userRatingDto = new UserRatingDto();
//            userRatingDto.setAverage(0.0);
//            userRatingDto.setCount(0);
//            userRatingDto.setUser(userDto);
//            userRatingService.save(userRatingDto);
//        });
//    }
//
//    private List<OrderCategoryDto> testOrderCategoryInit(){
//        return List.of(
//                OrderCategoryDto.builder().name("Grocery").build(),
//                OrderCategoryDto.builder().name("Music instruments").build(),
//                OrderCategoryDto.builder().name("Headset").build(),
//                OrderCategoryDto.builder().name("Electronics").build(),
//                OrderCategoryDto.builder().name("Clothing").build(),
//                OrderCategoryDto.builder().name("Footwear").build(),
//                OrderCategoryDto.builder().name("Books").build(),
//                OrderCategoryDto.builder().name("Home Appliances").build(),
//                OrderCategoryDto.builder().name("Furniture").build(),
//                OrderCategoryDto.builder().name("Sports Equipment").build(),
//                OrderCategoryDto.builder().name("Toys").build(),
//                OrderCategoryDto.builder().name("Beauty And Personal Care").build(),
//                OrderCategoryDto.builder().name("Automotive Accessories").build(),
//                OrderCategoryDto.builder().name("Pet Supplies").build(),
//                OrderCategoryDto.builder().name("Stationery And Office Supplies").build(),
//                OrderCategoryDto.builder().name("Gardening Tools").build(),
//                OrderCategoryDto.builder().name("Watches And Accessories").build(),
//                OrderCategoryDto.builder().name("Jewelry").build(),
//                OrderCategoryDto.builder().name("Health And Wellness").build(),
//                OrderCategoryDto.builder().name("Travel Accessories").build(),
//                OrderCategoryDto.builder().name("Baby Products").build(),
//                OrderCategoryDto.builder().name("Gaming Consoles And Accessories").build(),
//                OrderCategoryDto.builder().name("Kitchen Essentials").build()
//        );
//    }
//
//    private List<AddressDto> testAddressInit(){
//        return List.of(
//                AddressDto.builder().street("Baker Street 221B").city("London").state("England").zip("NW1 6XE").country("United Kingdom").build(),
//                AddressDto.builder().street("Champs-Élysées 30").city("Paris").state("Île-de-France").zip("75008").country("France").build(),
//                AddressDto.builder().street("Alexanderplatz 1").city("Berlin").state("Berlin").zip("10178").country("Germany").build(),
//                AddressDto.builder().street("Viale della Conciliazione 5").city("Rome").state("Lazio").zip("00193").country("Italy").build(),
//                AddressDto.builder().street("Gran Via 28").city("Madrid").state("Community of Madrid").zip("28013").country("Spain").build(),
//                AddressDto.builder().street("Damrak 1").city("Amsterdam").state("North Holland").zip("1012 LG").country("Netherlands").build(),
//                AddressDto.builder().street("Nyhavn 17").city("Copenhagen").state("Capital Region").zip("1051").country("Denmark").build(),
//                AddressDto.builder().street("Karl Johans gate 45").city("Oslo").state("Oslo").zip("0162").country("Norway").build(),
//                AddressDto.builder().street("Mannerheimintie 10").city("Helsinki").state("Uusimaa").zip("00100").country("Finland").build(),
//                AddressDto.builder().street("Kungsportsavenyen 5").city("Gothenburg").state("Västra Götaland").zip("41136").country("Sweden").build(),
//                AddressDto.builder().street("Stephansplatz 1").city("Vienna").state("Vienna").zip("1010").country("Austria").build(),
//                AddressDto.builder().street("Náměstí Republiky 3").city("Prague").state("Prague").zip("11000").country("Czech Republic").build(),
//                AddressDto.builder().street("Krakowskie Przedmieście 13").city("Warsaw").state("Masovian").zip("00-071").country("Poland").build(),
//                AddressDto.builder().street("Váci utca 15").city("Budapest").state("Central Hungary").zip("1052").country("Hungary").build(),
//                AddressDto.builder().street("Vasari Passage 2").city("Florence").state("Tuscany").zip("50122").country("Italy").build(),
//                AddressDto.builder().street("Rue du Rhône 8").city("Geneva").state("Geneva").zip("1204").country("Switzerland").build(),
//                AddressDto.builder().street("Place des Palais 1").city("Brussels").state("Brussels").zip("1000").country("Belgium").build(),
//                AddressDto.builder().street("O'Connell Street 10").city("Dublin").state("Leinster").zip("D01 F5P2").country("Ireland").build(),
//                AddressDto.builder().street("Strøget 45").city("Copenhagen").state("Capital Region").zip("1160").country("Denmark").build(),
//                AddressDto.builder().street("Avenida da Liberdade 50").city("Lisbon").state("Lisbon").zip("1250-145").country("Portugal").build(),
//                AddressDto.builder().street("Mickiewicza 7").city("Kraków").state("Lesser Poland").zip("31-120").country("Poland").build(),
//                AddressDto.builder().street("Strada Lipscani 25").city("Bucharest").state("Bucharest").zip("030031").country("Romania").build(),
//                AddressDto.builder().street("Wenceslas Square 42").city("Prague").state("Prague").zip("11000").country("Czech Republic").build(),
//                AddressDto.builder().street("Trg bana Jelačića 1").city("Zagreb").state("Zagreb").zip("10000").country("Croatia").build(),
//                AddressDto.builder().street("Strada Magheru 12").city("Bucharest").state("Bucharest").zip("010332").country("Romania").build(),
//                AddressDto.builder().street("Alexander Nevsky Square 2").city("Sofia").state("Sofia City Province").zip("1000").country("Bulgaria").build(),
//                AddressDto.builder().street("Plaça de Catalunya 3").city("Barcelona").state("Catalonia").zip("08002").country("Spain").build(),
//                AddressDto.builder().street("Andrassy Avenue 20").city("Budapest").state("Central Hungary").zip("1061").country("Hungary").build(),
//                AddressDto.builder().street("Knez Mihailova 14").city("Belgrade").state("Belgrade").zip("11000").country("Serbia").build(),
//                AddressDto.builder().street("Rue de la Loi 175").city("Brussels").state("Brussels").zip("1040").country("Belgium").build()
//        );
//    }
//
//    private List<OrderDto> testOrderInit(List<UserDto> userDtos, List<OrderCategoryDto> orderCategoryDtos,
//                                         List<AddressDto> addressDtos){
//        List<OrderDto> orders = new ArrayList<>();
//        Random random = new Random();
//
//        List<String> weightUnits = List.of("kg", "lbs");
//        List<String> dimensionUnits = List.of("cm", "inch", "m", "foot");
//        List<String> currencies = List.of("EUR", "USD", "GBP", "CHF", "PLN", "UAH");
//
//        for (UserDto user : userDtos) {
//            for (int i = 0; i < 10; i++) {
//                int categoryCount = random.nextInt(3) + 1;
//                Set<OrderCategoryDto> categories = new HashSet<>();
//                for (int j = 0; j < categoryCount; j++) {
//                    categories.add(orderCategoryDtos.get(random.nextInt(orderCategoryDtos.size())));
//                }
//
//                double weight = 0.5 + (random.nextDouble() * 49.5);
//                String weightUnit = weightUnits.get(random.nextInt(weightUnits.size()));
//
//                String dimensionUnit = dimensionUnits.get(random.nextInt(dimensionUnits.size()));
//                String dimensions;
//                if (dimensionUnit.equals("m") || dimensionUnit.equals("foot")) {
//                    double dim1 = 0.1 + (random.nextDouble() * 4.9); // 0.1 to 5
//                    double dim2 = 0.1 + (random.nextDouble() * 4.9);
//                    double dim3 = 0.1 + (random.nextDouble() * 4.9);
//                    dimensions = String.format("%.1fx%.1fx%.1f", dim1, dim2, dim3);
//                } else {
//                    int dim1 = 10 + random.nextInt(191);
//                    int dim2 = 10 + random.nextInt(191);
//                    int dim3 = 10 + random.nextInt(191);
//                    dimensions = String.format("%dx%dx%d", dim1, dim2, dim3);
//                }
//
//                double cost = 10 + (random.nextDouble() * 300);
//                String currency = currencies.get(random.nextInt(currencies.size()));
//
//                int departureIdx = random.nextInt(addressDtos.size());
//                int deliveryIdx;
//                do {
//                    deliveryIdx = random.nextInt(addressDtos.size());
//                } while (deliveryIdx == departureIdx);
//
//                int day = random.nextInt(24) + 1;
//                int month = random.nextInt(12) + 1;
//                Date creationDate = Date.valueOf(String.format("2025-%02d-%02d", month, day));
//
//                String additionalInfo = random.nextBoolean() ? "Fragile items" : "Handle with care";
//
//                OrderDto order = OrderDto.builder()
//                        .status(OrderStatus.PUBLISHED)
//                        .categories(categories)
//                        .departureAddress(addressDtos.get(departureIdx))
//                        .deliveryAddress(addressDtos.get(deliveryIdx))
//                        .weight(String.format("%.1f", weight))
//                        .weightUnit(weightUnit)
//                        .dimensions(dimensions)
//                        .dimensionsUnit(dimensionUnit)
//                        .cost(String.format("%.2f", cost))
//                        .currency(currency)
//                        .additionalInfo(additionalInfo)
//                        .creationDate(creationDate)
//                        .customer(user)
//                        .build();
//
//                orders.add(order);
//            }
//        }
//
//        return orders;
//    }
//}