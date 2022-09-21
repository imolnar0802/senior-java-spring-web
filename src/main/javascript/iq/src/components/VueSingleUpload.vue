<template>

    <form class="dropzone" ref="dz">
        <div class="fallback">
            <input name="file" type="file" multiple/>
        </div>
    </form>

</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Dropzone from 'dropzone';

    @Component
    export default class VueSingleUpload extends Vue {
        private myDropzone: Dropzone | null = null;

        public mounted() {
            this.myDropzone = new Dropzone(this.$refs.dz as HTMLElement, {
                uploadMultiple:false,
                clickable:true,
                addRemoveLinks:true,
                url: '/api/file/post',
            });

            this.myDropzone.on("success", (file: Dropzone.DropzoneFile, response: Object | string) => {
                this.$emit("finished", file);
            })
        }

        public destroyed() {
            if (this.myDropzone) {
                this.myDropzone.destroy();
                this.myDropzone = null;
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">

    //FIXME
    $light-border-gray: #dedede;
    $trd-green: #68CEA2;
    $sec-dark-gray: #555;
    $light-blue: #378caf;
    $pri-green: #21A96F;
    $blue: #276b9f;
    $pri-gray: #cccccc;
    $grapefruit-hover: #DA4453;

    .uploader-single-file {
        $iconWidth: 70px;
        $iconHeight: 40px;
        $thumbnailSize: 100px;
        $padding: 10px;
        min-height: $thumbnailSize + $padding * 2;
        border: 1px solid $light-border-gray;
        border-radius: 2px;
        position: relative;
        z-index: 0;

        .uploader-dropzone {
            overflow: hidden;
            transition: 0.3s ease visibility, 0.3s ease opacity, 0.3s ease transform;

            .disable-animations & {
                transition-duration: 0s;
            }

            position: absolute;
            width: 100%;
            height: 100%;
            left: 0;
            top: 0;
            z-index: 3;
            text-align: center;

            &::before {
                content: "";
                display: block;
                height: 50%;
                padding-bottom: $iconHeight / 2;
            }

            .uploader-dropzone-label {
                margin: 0 10px 10px 10px;
            }

            .file-selector {
                cursor: pointer;
                position: absolute;
                width: 100%;
                height: 100%;
                top: 0;
                left: 0;

                &.file-selector-drag-over {
                    background-color: rgba($trd-green, 0.5);
                }

                form {
                    cursor: pointer;
                    position: absolute;
                    z-index: 1;
                    opacity: 0;
                    display: none;

                    input {
                        cursor: pointer;
                    }
                }
            }
        }

        //.uploader-icon {
        //  position: absolute;
        //  left: 50%;
        //  top: 50%;
        //  margin-left: -$iconWidth/2;
        //  margin-top: -$iconHeight/2;
        //  height: $iconHeight;
        //  transition: 0.3s ease margin-left, 0.3s ease left, 0.3s ease opacity;
        //  .disable-animations & {
        //    transition-duration: 0s;
        //  }
        //  &::before {
        //    display: block;
        //    line-height: $iconHeight;
        //    background-repeat: no-repeat;
        //    background-position: center;
        //    background-size: 24px;
        //    background-image: svg-icon-upload($sec-dark-gray, $light-blue);
        //    text-align: center;
        //    content: '';
        //    width: $iconWidth;
        //    height: $iconHeight;
        //    top: 50%;
        //  }
        //}
        .uploader-screens {
            position: relative;
            box-sizing: border-box;
            opacity: 0;
            visibility: hidden;
            transform: translateX(20px);
            transition: 0.3s ease visibility, 0.3s ease opacity, 0.3s ease transform;

            .disable-animations & {
                transition-duration: 0s;
            }

            z-index: 2;
            padding: 0;

            &::after {
                display: table;
                clear: both;
                content: "";
            }

            .uploader-screen {
                display: none;

                &.active {
                    display: block;
                }
            }

            ul {
                margin: 0;
                padding: 0;
                display: table;
                list-style-type: none;
                margin-bottom: 10px;

                li {
                    display: table-row;
                    margin: 0;
                    padding: 0;

                    .label {
                        padding-right: 5px;
                        font-weight: bold;
                        display: table-cell;
                        white-space: nowrap;
                        width: 0;

                        &::after {
                            content: ":";
                        }
                    }

                    .value {
                        display: table-cell;
                        word-break: break-all;

                        a {
                            display: inline-block;
                        }

                        &.error-message {
                            color: $grapefruit-hover;
                            font-size: inherit;
                        }
                    }
                }
            }
        }

        .uploader-progress-bar {
            position: absolute;
            top: 0;
            left: 0;
            height: 0;
            background-color: $pri-green;
            width: 0;
            transition: 0.3s cubic-bezier(0, 0, 0.5, 1) width, 0.3s ease height;

            .disable-animations & {
                transition-duration: 0s;
            }

            z-index: 1;
            border-top-left-radius: 1px;
            border-top-right-radius: 1px;

            &.uploader-progress-bar-show {
                height: 5px;
            }
        }

        &[data-upload-state="processing"] {
            .uploader-progress-bar {
                background-color: $blue;
            }
        }

        .uploader-thumbnail {
            width: $thumbnailSize;
            height: $thumbnailSize;
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center center;
            background-color: $pri-gray;
        }

        &[data-compact="false"] {
            &::before {
                display: block;
                content: "";
                top: 25%;
                height: 50%;
                width: 1px;
                position: absolute;
                left: $iconWidth;
                background-color: $light-border-gray;
                transition: 0.3s ease transform;

                .disable-animations & {
                    transition-duration: 0s;
                }

                transform: scaleY(0);
            }

            .uploader-screens {
                margin-left: $iconWidth;
                padding-left: $padding * 2;
            }

            .uploader-thumbnail-container {
                position: absolute;
                opacity: 0;
                visibility: hidden;
                right: $padding;
                transition: 0.3s ease opacity, 0.3s ease visibility;

                .disable-animations & {
                    transition-duration: 0s;
                }
            }

            .uploader-show-thumbnail {
                .uploader-screens-info {
                    margin-right: $thumbnailSize + $padding;
                }

                .uploader-thumbnail-container {
                    opacity: 1;
                    visibility: inherit;
                }
            }
        }

        &[data-compact="true"] {
            .uploader-thumbnail-container {
                height: 0;
                opacity: 0;
                transition: 0.3s ease opacity, 0.3s ease height;

                .disable-animations & {
                    transition-duration: 0s;
                }

                overflow: hidden;
                width: $thumbnailSize;
                margin: 0 auto;
            }

            &.uploader-show-screens {
                .uploader-icon {
                    opacity: 0;
                }

                .uploader-show-thumbnail {
                    .uploader-thumbnail-container {
                        opacity: 1;
                        height: $thumbnailSize + $padding;
                    }
                }
            }
        }

        &.uploader-show-screens {
            &::before {
                transform: scaleY(1);
            }

            .uploader-icon {
                left: 0;
                margin-left: 0;
            }

            .uploader-screens {
                opacity: 1;
                visibility: inherit;
                transform: none;
            }

            .uploader-dropzone {
                opacity: 0;
                visibility: hidden;
                transform: translateY(20px);
            }
        }
    }

</style>
