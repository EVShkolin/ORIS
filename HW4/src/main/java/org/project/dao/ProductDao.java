package org.project.dao;

import org.project.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ProductDao {

    public ProductDao() {
        File file = new File("data/products.txt");
        if (!file.exists()) {
            init();
        }
    }

    public List<Product> getAllProducts() {
        try {
            return Files.lines(Path.of("data/products.txt"))
                    .map(line -> line.split("#"))
                    .map(x -> new Product(
                                Long.valueOf(x[0]),
                                x[1],
                                new BigDecimal(x[2]),
                                x[3]))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(List<Product> products) {
        try(var bw = new BufferedWriter(new FileWriter("data/cart.txt"))) {
            for (Product product : products) {
                StringBuilder sb = new StringBuilder();
                sb.append(product.getId());
                sb.append("#");
                sb.append(product.getName());
                sb.append("#");
                sb.append(product.getQuantity());
                sb.append("#");
                sb.append(product.getPrice());
                sb.append("#");
                sb.append(product.getImageUrl());
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {}
    }

    private void init() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try (var bw = new BufferedWriter(new FileWriter("data/products.txt"))) {
            bw.write("""
            1#Смартфон Xiaomi Redmi Note 13#21999#https://mtscdn.ru/upload/iblock/8ae/12_7azp_2x_jpg.png
            2#Беспроводные наушники Sony WH-1000XM5#29990#https://doctorhead.ru/upload/dev2fun.imagecompress/webp/iblock/81e/5zure0rbjtg5lv3nvxktztkaflzycvg3/sony_wh1000xm5_beige_silver.webp
            3#Умные часы Apple Watch Series 9#40990#https://world-devices.ru/image/cache/catalog/1/cmyuqoe0swig2g7x0ex8h99nfu6rlh60-500x400.jpg
            4#Футболка хлопковая с принтом#1499#https://cdn1.boscooutlet.ru/upload/iblock/a36/a362cbcbc181767570b110f940e5fec2_1221_1607.jpg
            5#Игровая консоль PlayStation 5#59999#https://ir.ozone.ru/s3/multimedia-0/c1000/6837907680.jpg
            6#Книга "Мастер и Маргарита"#850#https://ir.ozone.ru/s3/multimedia-f/c1000/6151150443.jpg
            7#Кофеварка капельная De'Longhi#12499#https://delonghi.ru/upload/resize_cache/iblock/92f/lrmibp43gt89m5q84leav2lw74i4dex7/600_700_1/ICM16731_FR_BLUE.jpg
            8#Рюкзак  городской  Herschel#5490#https://www.blacksides.ru/upload/resize_cache/iblock/28c/998_2500_1/dukk203qwaf1i6401221bfc3ytce9i2t.jpg
            9#Набор  кистей  для  макияжа#899#https://ir.ozone.ru/s3/multimedia-4/c1000/6862626472.jpg
            10#Внешний аккумулятор Power Bank 20000 mAh#3290#https://main-cdn.sbermegamarket.ru/big1/hlr-system/47/75/76/95/14/17/100026391067b0.jpg
            """);
        } catch (IOException e) {
            System.out.println("Could not write to file data/products.txt");
        }
    }

}
