<div align="center">
  <h1>ItemUtils</h1>
    
  _**ItemUtils** is a simple set of utility classes for working with custom item plugins on PaperMC servers. It's intended purpose is to make adding custom item support to your plugins effortless._

<br>
<div>
<a href="https://github.com/milkdrinkers/ItemUtils/blob/main/LICENSE">
    <img alt="GitHub License" src="https://img.shields.io/github/license/milkdrinkers/ItemUtils?style=for-the-badge&color=blue&labelColor=141417">
</a>
<a href="https://central.sonatype.com/artifact/io.github.milkdrinkers/itemutils">
    <img alt="Maven Central Version" src="https://img.shields.io/maven-central/v/io.github.milkdrinkers/itemutils?style=for-the-badge&labelColor=141417">
</a>
<a href="https://javadoc.io/doc/io.github.milkdrinkers/itemutils">
    <img alt="Javadoc" src="https://img.shields.io/badge/JAVADOC-8A2BE2?style=for-the-badge&labelColor=141417">
</a>
<img alt="GitHub Actions Workflow Status" src="https://img.shields.io/github/actions/workflow/status/milkdrinkers/ItemUtils/ci.yml?style=for-the-badge&labelColor=141417">
<br>

<a href="https://github.com/milkdrinkers/ItemUtils/issues">
    <img alt="GitHub Issues" src="https://img.shields.io/github/issues/milkdrinkers/ItemUtils?style=for-the-badge&labelColor=141417">
</a>
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/milkdrinkers/ItemUtils?style=for-the-badge&labelColor=141417">
<a href="https://discord.gg/cG5uWvUcM6">
    <img alt="Discord Server" src="https://img.shields.io/discord/1008300159333040158?style=for-the-badge&logo=discord&logoColor=ffffff&label=discord&labelColor=141417&color=%235865F2">
</a>
</div>
</div>

---

## 🌟 Features

- **Plugin Support**: 
  - **Nexo**
  - **Oraxen**
  - **ItemsAdder**

## 📦 Installation

Add ItemUtils to your project with **Maven** or **Gradle**:

<details>
<summary>Gradle Kotlin DSL</summary>

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.milkdrinkers:itemutils:LATEST_VERSION")
}
```

</details>

<details>
<summary>Maven</summary>

```xml
<project>
    <dependencies>
        <dependency>  
            <groupId>io.github.milkdrinkers</groupId>  
            <artifactId>itemutils</artifactId>  
            <version>LATEST_VERSION</version>  
        </dependency>  
    </dependencies>
</project>
```

</details>

## Usage Example 🚀

```java
import io.github.milkdrinkers.itemutil.ItemUtils;
import io.github.milkdrinkers.itemutil.InventoryUtils;

// Get item stack by item id

// Vanilla
ItemStack diamondSword1 = ItemUtils.parse("minecraft:diamond_sword");
ItemStack diamondSword2 = ItemUtils.parse("diamond_sword"); // By default assumes using "minecraft:" namespace
ItemStack diamondSword3 = ItemUtils.parseOptional("diamond_sword"); // Same as #parse(String), but returns an optional

// Custom items from plugins
ItemStack nexoItem = ItemUtils.parse("nexo:custom_item_name"); // Nexo
ItemStack oraxenItem = ItemUtils.parse("oraxen:custom_item_name"); // Oraxen
ItemStack itemsAdderItem = ItemUtils.parse("itemsadder:custom_item_name"); // ItemsAdder

player.getInventory().addItem(diamondSword1);

// Get item id from item stack
String itemId1 = ItemUtils.parse(diamondSword1); // Returns "minecraft:diamond_sword"
String itemId2 = ItemUtils.parse(nexoItem); // Returns "nexo:custom_item_name"

// Check if an item/materials exists with this item id
boolean doesExist = ItemUtils.exists("itemsadder:some_randon_item_name");

// Check if player has custom item in their inventory
boolean hasEnough = InventoryUtils.contains(player, "nexo:custom_item_name", 1); // Check if player has at least 1 of the custom item

// Remove custom items from player's inventory
InventoryUtils.removeItem(player, "nexo:custom_item_name", 5); // Remove 5 of the custom item from player's inventory
```

## 📚 Documentation

- [Full Javadoc Documentation](https://javadoc.io/doc/io.github.milkdrinkers/itemutils)
- [Maven Central](https://central.sonatype.com/artifact/io.github.milkdrinkers/itemutils)

---

## 🔨 Building from Source

```bash
git clone https://github.com/milkdrinkers/ItemUtils.git
cd itemutils
./gradlew publishToMavenLocal
```

---

## 🔧 Contributing

Contributions are always welcome! Please make sure to read our [Contributor's Guide](CONTRIBUTING.md) for standards and
our [Contributor License Agreement (CLA)](CONTRIBUTOR_LICENSE_AGREEMENT.md) before submitting any pull requests.

We also ask that you adhere to our [Contributor Code of Conduct](CODE_OF_CONDUCT.md) to ensure this community remains a
place where all feel welcome to participate.

---

## 📝 Licensing

You can find the license the source code and all assets are under [here](../LICENSE). Additionally, contributors agree
to the Contributor License Agreement \(*CLA*\) found [here](CONTRIBUTOR_LICENSE_AGREEMENT.md).

---

## 🔥 Consuming Projects

Here is a list of known projects using ItemUtils:
- (*Add your project here!*)