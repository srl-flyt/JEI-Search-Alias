# JEI Search Alias

MinecraftのJEIに検索用のエイリアスを追加するクライアントサイドModです。
<br>
## 対応環境

forge 1.20.1
<br>
作者の気分によって増えるかもしれません。
<br>

## 機能

- Item IDでの検索ができます。
- .json形式でJEIのアイテム検索に任意のエイリアスを追加できます。
<br>

## エイリアスの追加方法
例えば、`ダイヤモンド`と`ダイヤモンドの剣`の検索にひらがな、だい"あ"の読みを対応させるとします。
```json
{
  "minecraft:diamond": [
    "だいやもんど",
    "だいあもんど"
  ],
  "minecraft:diamond_sword": [
    "だいやもんどのけん",
    "だいあもんどのけん"
  ]
}
```
このようにItem IDごとに割り当てます。<br>
または、
```json
{
  "minecraft:": [
    {
      "diamond": [
        "だいやもんど",
        "だいあもんど" 
      ],
      "diamond_sword": [
        "だいやもんどのけん",
        "だいあもんどのけん"
      ]
    }
  ]
}
```
のように`minecraft:`のようなNamespaceをまとめて指定することもできます。<br>
こうすることで、ダイヤモンドがひらがなでもJEIの検索結果に表示されるようになります。<br><br>
IDごとの割り当てとNamespaceの指定を混ぜて記述することも可能です。
```json
{
  {
    "minecraft:diamond": [
      "だいやもんど",
      "だいあもんど"
    ]
  },
  "minecraft:": [
    {
      "diamond_sword": [
        "だいやもんどのけん",
        "だいあもんどのけん"
      ]
    }
  ]
}
```
## ファイルの保存場所
上のように作ったファイルは
```text
config/jei_search_alias/
```
に置くことで読み込まれるようになります。<br><br>
ファイル数や名前に指定はありません。
```
config/
└─ jei_search_alias/
   ├─ vanilla.json
   ├─ mekanism.json
   └─ create.json
```
のようにmodごとに分けて配置することが出来ます。

## 注意
### ゲーム内でエイリアスの再読み込みは出来ません。<br>
ファイルを更新したい場合、一度Minecraftを再起動してください。
