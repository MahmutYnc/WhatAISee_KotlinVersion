/*const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp()
const spawn = require('child-process-promise').spawn;
const path = require('path');
const os = require('os');
const fs = require('fs');


// [START generateThumbnailTrigger]
exports.generateThumbnail = functions.runWith({ memory: "2GB", timeoutSeconds: 60 }).region('europe-west2').storage.object().onFinalize(async (object) => {
    // [END generateThumbnailTrigger]
    // [START eventAttributes]
    
    const fileBucket = object.bucket; // The Storage bucket that contains the file.
    const filePath = object.name; // File path in the bucket.
    const contentType = object.contentType; // File content type.
    const metageneration = object.metageneration; // Number of times metadata has been generated. New objects have a value of 1.
    // [END eventAttributes]

    // [START stopConditions]
    // Exit if this is triggered on a file that is not an image.
    if (!contentType.startsWith('image/')) {
        return console.log('This is not an image.');
    }

    // Get the file name.
    const fileName = path.basename(filePath);
    // Exit if the image is already a thumbnail.
    if (fileName.startsWith('little')) {
        return console.log('Already a Thumbnail.');
    }
    // [END stopConditions]

    // [START thumbnailGeneration]
    // Download file from bucket.
    const bucket = admin.storage().bucket(fileBucket);
    const tempFilePath = path.join(os.tmpdir(), fileName);
    const metadata = {
        contentType: contentType,
    };
    await bucket.file(filePath).download({ destination: tempFilePath });
    console.log('Image downloaded locally to', tempFilePath);
    // Generate a thumbnail using ImageMagick.
    await spawn('convert', [tempFilePath, '-thumbnail', '500x500>', tempFilePath]);
    console.log('Thumbnail created at', tempFilePath);
    // We add a 'thumb_' prefix to thumbnails file name. That's where we'll upload the thumbnail.
    const thumbFileName = `little-${fileName}`;
    const thumbFilePath = path.join(path.dirname(filePath), thumbFileName);
    // Uploading the thumbnail.
    await bucket.upload(tempFilePath, {
        destination: thumbFilePath,
        metadata: metadata,
    });
    // Once the thumbnail has been uploaded delete the local file to free up disk space.
    return fs.unlinkSync(tempFilePath);
    // [END thumbnailGeneration]
});
// [END generateThumbnail]

*/

const functions = require("firebase-functions");
const { tmpdir } = require("os");
const { Storage } = require("@google-cloud/storage");
const { dirname, join } = require("path");
const sharp = require("sharp");
const fs = require("fs-extra");
const gcs = new Storage();

exports.resizeImg = functions
    .runWith({ memory: "2GB", timeoutSeconds: 120 })
    .region('europe-west2')
    .storage
    .object()
    .onFinalize(async (object) => {
        const bucket = gcs.bucket(object.bucket);
        const filePath = object.name;
        const fileName = filePath.split("/").pop();
        const bucketDir = dirname(filePath);

        const workingDir = join(tmpdir(), "resize");
        const tmpFilePath = join(workingDir, "source.png");

        console.log(`Got ${fileName} file`);

        if (fileName.includes("@s_") || !object.contentType.includes("image")) {
            console.log(`Already resized. Exiting function`);
            return false;
        }

        await fs.ensureDir(workingDir);
        await bucket.file(filePath).download({ destination: tmpFilePath });

        const sizes = [1920, 720, 400, 100];

        const uploadPromises = sizes.map(async (size) => {

            console.log(`Resizing ${fileName} at size ${size}`);

            const ext = fileName.split('.').pop();
            const imgName = fileName.replace(`.${ext}`, "");
            const newImgName = `${imgName}@s_${size}`;
            const imgPath = join(workingDir, newImgName);
            await sharp(tmpFilePath).resize({ width: size }).toFile(imgPath);

            console.log(`Just resized ${newImgName} at size ${size}`);

            return bucket.upload(imgPath, {
                destination: join(bucketDir, newImgName)
            });

        });

        await Promise.all(uploadPromises);

        return fs.remove(workingDir);

    });