
init_points = [ ...
    215, 788;
    232, 729;
    334, 727;
    425, 805;
    417, 719;
    512, 723;
    548, 690;
    582, 732;
    673, 640;
    633, 693;
    682, 685;
    736, 633;
    791, 652;
    766, 710;
    935, 762;
    981, 677;
    988, 590;
    1029, 568;
    1103, 556;
    1166, 548;
    1158, 610;
    1276, 518;
    1310, 497;
    1542, 566;
    1509, 519;
    1521, 471;
    1628, 452;
    1785, 551;
    1867, 426;
    ];

detector_filename = '/Users/mirkoraca/data/classroom/face_detections2.txt';
out_filename = '/Users/mirkoraca/results/classroom/face_rects2.txt';
img_folder = '/cvlabdata1/home/raca/data/classroom/canon_digital/imgs2'; %'/Users/mirkoraca/data/classroom/imgs';
out_format = '%d %d %d %d %d\n';
out_folder = '/Users/mirkoraca/results/classroom/imgs_corrected_rects2';

[col1, col2, col3, col4, col5] = textread(detector_filename, '%d %d %d %d %d');
tracks = [col1-2, col2, col3, col4, col5];
centers = [ (col2+col4)/2 (col3+col5)/2 ];
imgs = dir(fullfile(img_folder,'*.png'));

file_id = fopen(out_filename, 'w');
% init rects
num_of_pts_to_track = size(init_points,1);
out_rects{1} = zeros(num_of_pts_to_track,4);
for j = 1 : num_of_pts_to_track
    pt = init_points(j,:);
    out_rects{1}(j,:) = [pt(1)-20 pt(2)-20 pt(1)+20 pt(2)+20];
    fprintf(file_id, out_format, 1, out_rects{1}(j,:));
end

for i = 2 : length(imgs)
    % copy old rect information
    fprintf('Frame %d\n',i);
    out_rects{i} = out_rects{i-1};
    % load detected points
    cur_rects = tracks( tracks(:,1) == i, :);
    cur_centers = centers(tracks(:,1) == i, :);
    % compare detected points to expected ones
    %       next stop: use hungarian algorithm
    for j = 1 : num_of_pts_to_track
        % update the position where close enough
        pt = init_points(j,:);
        test_table = cur_centers;
        dists = (sum((test_table - repmat(pt,size(test_table,1),1)).^2,2)).^.5;
        [val, index] = min(dists);
        if val > 20
            continue; % jump too big, consider point not detected
        end
        init_points(j,:) = test_table(index,:);
        out_rects{i}(j,:) = cur_rects(index,2:end);
        % remove point from the further evaluation
        select_all = 1:size(cur_centers,1);
        select_all= setdiff(select_all,index);
        cur_centers = cur_centers(select_all,:);
        cur_rects = cur_rects(select_all,:);
        % write to txt file the final location
        fprintf(file_id, out_format, i, out_rects{i}(j,:));
    end
    % draw the output
    if i < 984
        continue;
    end
    im = imread(fullfile(img_folder, imgs(i).name));
    im_out = blurRegions(im,out_rects{i},20);
    imwrite(im_out, fullfile(out_folder, imgs(i).name),'png');
%     imshow(im);
%     for j = 1 : num_of_pts_to_track
%         todraw = out_rects{i}(j,:);
%         todraw(3:4) = todraw(3:4)-todraw(1:2);
%         rectangle('Position',todraw); % TODO: check, this could be wrong on x2,y2 coords
%     end
%     % save final output
%     saveas(1, fullfile(out_folder, imgs(i).name));
end

fclose(file_id);

disp('Done.');