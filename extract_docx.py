from docx import Document

# 打开文档
doc = Document('Android 计划管理App 项目文档（MVI + XML 架构版）.docx')

# 提取文本
text = []
for paragraph in doc.paragraphs:
    text.append(paragraph.text)

# 保存到文件
with open('document_content.txt', 'w', encoding='utf-8') as f:
    f.write('\n'.join(text))

print('文档内容已提取到 document_content.txt 文件')