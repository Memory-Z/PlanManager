# 解压DOCX文件
dir "Android 计划管理App 项目文档（MVI + XML 架构版）.docx" | Expand-Archive -DestinationPath "temp_docx" -Force

# 读取并解析document.xml
$xmlContent = Get-Content "temp_docx\word\document.xml" -Encoding UTF8

# 提取文本
$text = Select-Xml -Content $xmlContent -XPath "//text()" | ForEach-Object { $_.Node.Value.Trim() } | Where-Object { $_ -ne "" }

# 保存到文件
$text | Out-File "document_content.txt" -Encoding UTF8

# 清理临时文件夹
Remove-Item "temp_docx" -Recurse -Force

Write-Host "文档内容已提取到 document_content.txt 文件"