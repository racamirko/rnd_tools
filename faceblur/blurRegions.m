function outimg = blurRegions( img, rects, buff )
%BLURREGIONS Summary of this function goes here
%   Detailed explanation goes here
    outimg = img;
    if nargin < 3
        buff = 5;
    end
    for i = 1: size(rects,1)
        r = rects(i,:);
        r = [r(1)-buff r(2)-buff r(3)+buff r(4)+buff];
        subpart = img(r(2):r(4),r(1):r(3),:);
        for j = 1 : 4
            subpart = gaussianBlur(subpart);
        end
        outimg(r(2):r(4),r(1):r(3),:) = subpart;
    end

end

