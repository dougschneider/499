from PIL import Image, ImageFilter

image = Image.open('18.jpg')
image = image.filter(ImageFilter.EDGE_ENHANCE_MORE)
image.save('output18.jpg')
