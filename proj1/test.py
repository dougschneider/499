from PIL import Image, ImageFilter, ImageEnhance

image = Image.open('18.jpg')
image = image.convert('L')
#editor = ImageEnhance.Contrast(image)
#image = editor.enhance(1.3)
#image = image.enhance(ImageFilter.EDGE_ENHANCE_MORE)
image.save('output18.jpg')
