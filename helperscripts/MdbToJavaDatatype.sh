#!/bin/bash

echo "// === Java Field Declarations ==="
jq -r '
  def toJavaType(type):
    if type == "TEXT" then "String"
    elif type == "LONG" then "Long"
    elif type == "DOUBLE" then "Double"
    elif type == "BOOLEAN" then "Boolean"
    elif type == "SHORT_DATE_TIME" then "LocalDateTime"
    elif type == "MEMO" then "String"
    elif type == "MONEY" then "BigDecimal"
    else "String" end;

  def camelCase(name):
    name[0:1] | ascii_downcase + name[1:];

  .[0].columns[] |
  . as $col |
  $col.name as $name |
  toJavaType($col.type) as $javaType |
  camelCase($name) as $fieldName |
  (
    (if $name | ascii_downcase == "workorderid" then "@Id\n" else "" end) +
    (if $col.type == "MEMO" then "@Lob\n" else "" end) +
    (if $col.type == "TEXT" and $col.length > 0 then "@Column(length = \($col.length))\n" else "" end) +
    "private \($javaType) \($fieldName);\n"
  )
' "$@"

echo
echo "// === Full Constructor ==="
echo -n "public Workorder("
jq -r '
  def toJavaType(type):
    if type == "TEXT" then "String"
    elif type == "LONG" then "Long"
    elif type == "DOUBLE" then "Double"
    elif type == "BOOLEAN" then "Boolean"
    elif type == "SHORT_DATE_TIME" then "LocalDateTime"
    elif type == "MEMO" then "String"
    elif type == "MONEY" then "BigDecimal"
    else "String" end;

  def camelCase(name): name[0:1] | ascii_downcase + name[1:];

  .[0].columns[] |
  { type: toJavaType(.type), name: camelCase(.name) }
' "$@" | jq -rs '
  map("\(.type) \(.name)") | join(", ")
'
echo ") {"

jq -r '
  def camelCase(name): name[0:1] | ascii_downcase + name[1:];

  .[0].columns[] |
  .name as $name |
  camelCase($name) as $fieldName |
  "    this.\($fieldName) = \($fieldName);"
' "$@"

echo "}"
