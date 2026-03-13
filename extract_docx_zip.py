import zipfile
import xml.etree.ElementTree as ET

# 打开DOCX文件（实际上是ZIP文件）
with zipfile.ZipFile('Android 计划管理App 项目文档（MVI + XML 架构版）.docx', 'r') as zf:
    # 读取document.xml文件
    with zf.open('word/document.xml') as f:
        xml_content = f.read().decode('utf-8')

# 解析XML
tree = ET.ElementTree(ET.fromstring(xml_content))
root = tree.getroot()

# 提取文本
text = []
for element in root.iter():
    if element.text and element.text.strip():
        text.append(element.text.strip())

# 保存到文件
with open('document_content.txt', 'w', encoding='utf-8') as f:
    f.write('\n'.join(text))

print('文档内容已提取到 document_content.txt 文件')