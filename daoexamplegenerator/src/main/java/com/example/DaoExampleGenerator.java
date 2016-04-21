package com.example;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class DaoExampleGenerator {
    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1, "com.birdex.bird.greendao");
//      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
//      Schema schema = new Schema(1, "me.itangqi.bean");
//      schema.setDefaultJavaPackageDao("me.itangqi.dao");

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        addNotifi(schema);
        addWarehouse(schema);
        addMarket(schema);
        addPriceUnit(schema);
        addServiceType(schema);
        addBox(schema);
        addQgModel(schema);
        addTape(schema);
        addBusinessModel(schema);
        addTicket(schema);
        addCity(schema);
        File file = new File("");
        System.out.println(file.getAbsolutePath());
//        printDirectory(new File("../"));
        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
//        new DaoGenerator().generateAll(schema, "/Users/huwei/Documents/AndroidProject/Birdex/Birdex/app/src/main/java");
        new DaoGenerator().generateAll(schema, "D:" + File.separator + "newbirdcode" + File.separator + "Birdex" + File.separator + "app" + File.separator + "src" + File.separator + "main" + File.separator + "java");
    }

    public static void printDirectory(File file) {
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                printDirectory(childFile);
            }
            System.out.println(childFile.getName() + "/");
        }
    }

    /**
     * @param schema
     */
    private static void addNotifi(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("NotifiMsg");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        note.addIdProperty();

        //消息标题
        note.addStringProperty("title").notNull();
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        //消息内容
        note.addStringProperty("msgtext");
        //是否已读
        note.addBooleanProperty("isread");
        //消息接收时间
        note.addStringProperty("msgdate");
        //消息类型
        note.addStringProperty("typeid");
//        //消息里的字段参数
//        note.addStringProperty("params");
    }


    private static void addWarehouse(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("warehouse");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("sort");
        note.addStringProperty("iconUrl");
        note.addStringProperty("tel");
        note.addStringProperty("postCode");
        note.addStringProperty("warehouseId");
        note.addStringProperty("city");
        note.addStringProperty("country");
        note.addStringProperty("id");
        note.addStringProperty("name");
        note.addStringProperty("addressInfo");
        note.addStringProperty("province");
        note.addStringProperty("district");
        note.addStringProperty("isShow");
        note.addStringProperty("mapUrl");
    }


    private static void addMarket(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("market");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("id");
        note.addStringProperty("status");
        note.addStringProperty("display_order");
        note.addStringProperty("name");
    }

    private static void addPriceUnit(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("priceUnit");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("id");
        note.addStringProperty("name");
    }

    private static void addServiceType(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("servicetype");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("key");
        note.addStringProperty("id");
        note.addStringProperty("is_show");
        note.addStringProperty("name");
    }

    private static void addBox(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("box");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("id");
        note.addStringProperty("type");
        note.addStringProperty("status");
        note.addStringProperty("display_order");
        note.addStringProperty("name");
    }

    private static void addQgModel(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("qgmodel");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("id");
        note.addStringProperty("status");
        note.addStringProperty("description");
        note.addStringProperty("display_order");
        note.addStringProperty("name");
    }

    private static void addTape(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("tape");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("id");
        note.addStringProperty("type");
        note.addStringProperty("status");
        note.addStringProperty("display_order");
        note.addStringProperty("name");
    }

    private static void addBusinessModel(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("businessmodel");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("id");
        note.addStringProperty("status");
        note.addStringProperty("display_order");
        note.addStringProperty("name");
    }

    private static void addTicket(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("ticket");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
//        note.addIdProperty();

        note.addStringProperty("id");
        note.addStringProperty("type");
        note.addStringProperty("status");
        note.addStringProperty("display_order");
        note.addStringProperty("name");
    }

    private static void addCity(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("city");
        // 你也可以重新给表命名
        // note.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        note.addIdProperty();

        note.addStringProperty("AreaID");
        note.addStringProperty("AreaName");
        note.addStringProperty("ParentID");
    }

}
