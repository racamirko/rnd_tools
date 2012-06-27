function blurredIm = gaussianBlur(im)
%%%
%%%  Copyright CVLab @ EPFL (c) 2010
%%%


im = double(im);
[h w channels] = size(im);
blurredIm = uint8(zeros(h,w,channels));
tmpIm = zeros(h,w,channels);

gauss_conv = [ 2 3 4 3 2 ];
coeff = 1/14;
submat = zeros( 5, 1 );

% horiz blur
for i = 1 : w
    for j = 1 : h
        for k = 1 : channels 
            for i1 = -2 : 2
                coord = i + i1;
                if coord < 1
                    coord = coord + w;
                end
                if coord > w
                    coord = coord - w;
                end
                submat(i1+3) = double(im(j, coord, k));
            end
            res = (gauss_conv * submat)*coeff;
            tmpIm( j, i, k) = round(res);
        end
    end
end

% vert blur
for i = 1 : w
    for j = 1 : h
        for k = 1 : channels 
            for i1 = -2 : 2
                coord = j + i1;
                if coord < 1
                    coord = coord + h;
                end
                if coord > h
                    coord = coord - h;
                end
                submat(i1+3) = tmpIm(coord, i, k);
            end
            res = (gauss_conv * submat)*coeff;
            blurredIm( j, i, k) = uint8(round(res));
        end
    end
end
