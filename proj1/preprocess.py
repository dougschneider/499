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
            "1:\t"
            "\n".format(sys.argv[0])

    n = None
    # all transforms available keyed by n
    options = {
                0: _t_0,
                1: _t_1,
                2: _t_2,
              }

    if len(sys.argv) > 1:
        n = int(sys.argv[1])
        data_dir = sys.argv[-1]

        # check if we have an action for n
        f = getattr(options, n, None)

        if n:
            apply_transform(data_dir)
        else:
            raise Exception(str(p) + " is not a valid value as the first argument.")
    else:
        print usage
        sys.exit(2)

