import sys
from scipy import ndimage
import os
from PIL import Image, ImageFilter, ImageEnhance


def _t_0(full_name):
    pass


def _t_1(full_name):
    image = Image.open(full_name)
    editor = ImageEnhance.Contrast(image)
    image = editor.enhance(1.5)
    image.save(full_name);


def _t_2(full_name):
    image = Image.open(full_name)
    image = image.crop((36, 0, 72, 43))
    image = image.filter(ImageFilter.EDGE_ENHANCE_MORE)
    image.save(full_name);


def _t_3(full_name):
    image = Image.open(full_name)
    image = image.crop((36, 0, 72, 43))
    editor = ImageEnhance.Contrast(image)
    image = editor.enhance(1.5)
    image.save(full_name);


def _t_4(full_name):
    image = Image.open(full_name)
    image = image.crop((36, 0, 72, 43))
    editor = ImageEnhance.Contrast(image)
    image = editor.enhance(1.5)
    image = image.filter(ImageFilter.EDGE_ENHANCE)
    image.save(full_name);


def _t_5(full_name):
    image = Image.open(full_name)
    image = image.crop((36, 0, 72, 43))
    image.save(full_name);


def _t_6(full_name):
    image = Image.open(full_name)
    image = image.crop((36, 0, 72, 43))
    image = image.convert('L')
    image.save(full_name);


def apply_transform(fcn, folder):
    for _, _, filenames in os.walk(folder):
        for filename in filenames:
            if filename.endswith(".jpg"):
                full_name = folder + "/" + filename
                fcn(full_name)


if __name__ == "__main__":
    """
    Apply the chosen transform to each image in the given dir.
    """
    usage = "Usage:\n"\
            "{} n data_dir\n\n"\
            "First parameter is required. Specify one of the following:\n"\
            "0:\tNo Transformations\n"\
            "1:\tIncrease Contrast\n"\
            "2:\tCrop and Edge Enhance\n"\
            "3:\tCrop and Contrast\n"\
            "4:\tCrop, Contrast, and Edge Enhance\n"\
            "5:\tCrop only\n"\
            "6:\tCrop and Greyscale\n"\
            "".format(sys.argv[0])

    n = None
    # all transforms available keyed by n
    options = {
                0: _t_0,
                1: _t_1,
                2: _t_2,
                3: _t_3,
                4: _t_4,
                5: _t_5,
                6: _t_6,
              }

    if len(sys.argv) > 1:
        n = int(sys.argv[1])
        data_dir = sys.argv[-1]

        # check if we have an action for n
        try:
            f = options[n]
        except Exception:
            f = None

        if f:
            apply_transform(f, data_dir)
        else:
            raise Exception(str(n) + " is not a valid value as the first argument.")
    else:
        print usage
        sys.exit(2)

